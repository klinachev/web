<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <article>
        <#list posts?reverse as p>
                <#if p.userId == user_id>
                    <@c.post p true/>
                </#if>
        </#list>
    </article>
</@c.page>
