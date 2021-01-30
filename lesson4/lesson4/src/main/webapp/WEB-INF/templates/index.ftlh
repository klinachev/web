<#import "./common.ftlh" as c>


<@c.page>
    <#list posts?reverse as p>
        <article>
            <@c.post p true/>
        </article>
    </#list>
</@c.page>
