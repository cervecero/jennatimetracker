<p><g:message code="email.salutation" locale="${recipient.locale}" args="${[recipient]}" /></p>
<p><g:message code="knowledge.heads.up.introduction" locale="${recipient.locale}" args="${[company, from, to]}" /></p>

<ul>
<g:each in="${ newKnowledge }" var="knowledge">
   <li>${knowledge.description} - ${knowledge.user}</li>
</g:each>
</ul>