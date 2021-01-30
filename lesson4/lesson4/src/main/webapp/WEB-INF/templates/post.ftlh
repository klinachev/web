<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <article>
        <#if post_id?? && post_id?is_number>
            <#assign post = c.findBy(posts, "id", post_id)!/>
        </#if>
        <#if post?? && post?has_content>
            <@c.post post false/>
        <#else>
            No such post
        </#if>
    </article>
</@c.page>
