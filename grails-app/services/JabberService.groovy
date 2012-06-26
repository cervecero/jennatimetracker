import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Message.Type
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smackx.packet.VCard
import org.springframework.beans.factory.InitializingBean
import org.jivesoftware.smack.*
import grails.util.GrailsUtil

/*
https://svn.codehaus.org/grails-plugins/grails-taggable/trunk/

http://pleac.sourceforge.net/pleac_groovy/strings.html
*/
class JabberService implements InitializingBean {

    boolean transactional = true
    def scope = 'singleton'

    ChatService chatService
    def grailsApplication

    private XMPPConnection connection
    Exception lastException

    public void afterPropertiesSet() {
        startListeningConversations()
    }

    def startListeningConversations() {
        try {
            connection = loginToJabber()
            updateCharacterInfo()
            registerMessageListener()
        } catch (Exception ex) {
            lastException = ex
        }
    }

    private def registerMessageListener() {
        connection.chatManager.addChatListener({Chat createdChat, boolean createdLocally ->
            // Just consider conversations created by other users
            // Add one listener for each created chat
            if (!createdLocally) {
                createdChat.addMessageListener(createMessageListener())
            }
        } as ChatManagerListener)

    }

    private def createMessageListener() {
        return {Chat messageChat, Message message ->
            if (message.type != Type.error) {
                handleIncomingMessage(messageChat, message)
            }
        } as MessageListener
    }

    private def handleIncomingMessage(Chat messageChat, Message message) {
        log.info "new message from $message.from: $message.body"
        User.withTransaction {
            def request = buildRequest(message)
            if (request) {
                try {
                    def response = chatService.handleRequest(request)
                    messageChat.sendMessage response
                } catch (Exception ex) {
                    log.error "Error processing the request from ${message?.from}: ${message?.body}", ex
                    if (GrailsUtil.isDevelopmentEnv()) {
                        StringWriter writer = new StringWriter()
                        ex.printStackTrace(new PrintWriter(writer))
                        messageChat.sendMessage "Error processing the request: $ex.message\n${writer.toString()}"
                    } else {
                        messageChat.sendMessage "Error processing the request: $ex.message"
                    }
                }
            }
        }
    }

    def static buildRequest(Message message) {
        def messageText = message?.body
        if (!message || !messageText) {
            return
        }
        def account = extractAccountFrom(message.from)
        if (!account) {
            return
        }
        User user = User.findByAccount(account)
        new Request(user: user, message: messageText)
    }

    /**
     * Get account id from jabber identification
     */
    private static def extractAccountFrom(String from) {
        def limitIndex = from.indexOf('/')
        return limitIndex == -1 ? null : from.substring(0, limitIndex)
    }

    /**
     * Update account config to jabber server
     */
    private def updateCharacterInfo() {
        try {
            VCard vCard = new VCard()
            vCard.load(connection)
            def config = getJabberConfiguration()
            vCard.firstName = config['firstname']
            vCard.lastName = config['lastname']
            vCard.organization = config['organization']
            def jabberAvatar = config['avatar']
            def avatarFile = Thread.currentThread().contextClassLoader.getResource(jabberAvatar)
            vCard.setAvatar(VCard.getBytes(avatarFile))
            vCard.save(connection)
        } catch (Exception ex) {
            // during Spring initialization the logging plugin isn't configured yet
            if (this.class.hasProperty('log')) {
                log.error 'Error updating vCard', ex
            }
        }
    }

    private def loginToJabber() {
        def config = getJabberConfiguration()
        String jabberServer = config['host']
        ConnectionConfiguration connConf = new ConnectionConfiguration(jabberServer)
        connConf.setSASLAuthenticationEnabled false
        connection = new XMPPConnection(connConf)
        connection.connect()

        def jabberUserName = config['username']
        def jabberPassword = config['password']

        connection.login(jabberUserName, jabberPassword)

        Presence presence = new Presence(Presence.Type.available)
        presence.status = config['status']
        connection.sendPacket(presence)

        // Accept all incoming invitations
        connection.roster.subscriptionMode = Roster.SubscriptionMode.accept_all

        //add myself to my contacts list to ping me for detecting disconnections
        if (!connection.roster.entries.user.contains(account.toString())) {
            connection.roster.createEntry(account, account)
        }

        def listener = [
            entriesAdded: { },
            entriesUpdated: { },
            entriesDeleted: { },
            presenceChanged: {/*
            // FIXME: This is not fully defined yet. Should we ask people to track yesterday's hours every time they forgot and change their status?
            Presence pr ->
                if (pr.type == Presence.Type.available) {
                    checkPreviousDateEffortsFor(extractAccountFrom(pr.from))
                }
                */
            }
        ]
        connection.roster.addRosterListener(listener as RosterListener)
        
        return connection
    }

    def addAccount(String account, String name){
      Roster roster = connection.getRoster()
      roster.createEntry(account, name, null);

    }

    def getAccount() {
        def config = getJabberConfiguration()
        def jaberServer = config['host']
        def jabberUserName = config['username']
        "$jabberUserName@$jaberServer"
    }

    boolean isJennaTalking() {
        return !this.lastException
    }

    def queryUsers(users) {
        users?.each { User user ->
            def presence = connection.roster.getPresence(user.account)
            if (presence?.available) {
                String askingMessage = chatService.requestTracking(user)
                Chat chat = createOutgoingChatWith(user)
                chat.sendMessage askingMessage
            }
        }
    }

    def queryUsersToInviteCoworkers(users) {
            users?.each { User user ->
                def presence = connection.roster.getPresence(user.account)
                if (presence?.available) {
                    String askingMessage = chatService.requestUserToInviteCoworker(user)
                    Chat chat = createOutgoingChatWith(user)
                    chat.sendMessage askingMessage
                }
            }
        }

    def remind(reminders) {
        reminders?.each { Reminder reminder ->
            def presence = connection.roster.getPresence(reminder.user.account)
            if (presence?.available) {
                String reminderMessage = chatService.remind(reminder)
                Chat chat = createOutgoingChatWith(reminder.user)
                chat.sendMessage reminderMessage
            }
            //TODO if user is not logged use another value for done
            reminder.done = true
            reminder.save()
        }
    }

    /**
     * Checks if the user tracked Efforts during the last "Chatting Day" and, if not,
     * reminds her to do it
     * FIXME: Not being used, as the listener that should invoke this is not configured
     */
    def checkPreviousDateEffortsFor(String _account) {
        Date today = new Date().onlyDate
        // TODO: Check if today is a "Chatting Day" ?
        User.withTransaction {
            User user = User.findByAccount(_account)
            Date previousDate = (today - 1).onlyDate // TODO: Was this a "Chatting Day"?
            if (!user.registeredEffortsFor(previousDate)) {
                String message = chatService.askForEfforts(user, previousDate)
                Chat chat = createOutgoingChatWith(user)
                chat.sendMessage message
            }
        }
    }

    def checkAndReconnect() {
        boolean gotPing = false
        try {
            connection.chatManager.createChat(account.toString(), {Chat messageChat, Message message ->
                gotPing = true
            } as MessageListener).sendMessage('ping')
            sleep(15000l)
        } catch (Exception ex) {
            // during Spring initialization the logging plugin isn't configured yet
            if (this.class.hasProperty('log')) {
                log.error 'Error in reconnection', ex
            }
        }
        if (!gotPing) {
            startListeningConversations()
        }
    }

    Chat createOutgoingChatWith(User _user) {
        String userJID = _user.account
        MessageListener messageListener = createMessageListener()
        Chat newChat = connection.chatManager.createChat(userJID, messageListener)
        return newChat
    }

    def getJabberConfiguration(){
        def config = grailsApplication.config
        return config['jabber']
    }

    boolean isValidAccount(String _mail) {
        def config = getJabberConfiguration()
        String jabberServer = config['host']
        return _mail.endsWith('@' + jabberServer)
    }
}
