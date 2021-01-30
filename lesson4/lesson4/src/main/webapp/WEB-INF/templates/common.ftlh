<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-vs-coronavirus-65.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user false/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/users", "name": "Users"},
            {"href": "/contests", "name": "Contests"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if href?starts_with(item["href"])>
                            <a href="${item.href}" class="underlined">${item.name}</a>
                        <#else>
                            <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts as p>
            <section>
                <div class="header">Post ${p.id}</div>
                <div class="body">
                    <@postText p true></@postText>
                </div>
                <div class="footer">
                    <a href="/post?post_id=${p.id}">View all</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2020 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user newStyle>
    <a href="/user?handle=${user.handle}"
       <#if newStyle?is_boolean && newStyle>
           class="${user.color}"
       </#if>
    >${user.name}</a>
</#macro>

<#macro arrowlink user>
    <#if !user?is_number>
        <a href="/user?handle=${user.handle}" class="arrow"><#nested></a>
    <#else>
        <#nested>
    </#if>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function findNextPrev items key value>
    <#list items as item>
        <#if item[key] == value>
            <#if 0 < item_index>
                <#assign prev = items[item_index - 1]>
            <#else>
                <#assign prev = 0>
            </#if>
            <#if item_index + 1 < items?size>
                <#assign next = items[item_index + 1]>
            <#else>
                <#assign next = 0>
            </#if>
            <#return {
                "prev" : prev,
                "item" : item,
                "next" : next
            }/>
        </#if>
    </#list>
</#function>

<#macro post p needCut>
        <div class="title">${p.title}</div>
        <div class="information">
            <#assign author = findBy(users,"id", p.userId)>
            <#if author??>
                <@userlink author true/>
            <#else>
                Unknown author
            </#if>
        </div>
        <div class="body">
            <@postText p needCut></@postText>
        </div>
</#macro>

<#macro postText post needCut>
    <#assign text = post.text>
    <#if needCut && 250 < text?length>
        <#assign txt = text?substring(0, 250)>
        ${txt}...
    <#else>
        ${text}
    </#if>
</#macro>

<#function count items key id>
    <#assign count = 0>
    <#list items as i>
        <#if i[key] == id>
            <#assign count = count + 1>
        </#if>
    </#list>
    <#return count/>
</#function>