import groovyx.twitter.Twitter

class TwitterService {

    def grailsApplication

    def twit(String _message) {
        def config = twitterConfiguration
        def username = config['username']
        def password = config['password']
        def twitter = new Twitter(username, password)
        twitter.postUpdate(_message)
        twitter.logoff()
    }

    def getTwitterConfiguration(){
        def config = grailsApplication.config
        return config['twitter']
    }
}
