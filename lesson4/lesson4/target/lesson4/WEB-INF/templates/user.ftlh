<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if handle??>
        <#assign u = c.findNextPrev(users, "handle", handle)!/>
    <#else>
        <#if user_id?? && user_id?is_number>
            <#assign u = c.findNextPrev(users, "id", user_id)!/>
        </#if>
    </#if>
    <#if u?? && u?has_content>
        <#assign item = u["item"]!/>
        <#assign prev = u["prev"]!/>
        <#assign next = u["next"]!/>
        <div class="user-block">
            <div>
                <div class="Head">
                    <@c.userlink item true/>
                </div>
                <ul>
                    <li>id: ${item.id}</li>
                    <li>handle: ${item.handle}</li>
                    <li>name: ${item.name}</li>
                    <li>
                        <#assign count = c.count(posts, "userId" , item.id)>
                        Posts count:
                        <a href="/posts?user_id=${item.id}">
                            ${count}
                        </a>
                    </li>
                </ul>
            </div>
            <img src="https://userpic.codeforces.com/no-title.jpg" alt="user picture" class="user-img">
        </div>
        <p class="arrows">
            <@c.arrowlink prev>&larr;</@c.arrowlink>
            <@c.arrowlink next>&rarr;</@c.arrowlink>
        </p>
    <#else>
        No such user
    </#if>
</@c.page>
