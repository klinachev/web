<#import "commons.ftlh" as c>

<@c.page>
    <#list user.posts as post>
        <@c.post macroPost=post/>
    </#list>
</@c.page>
