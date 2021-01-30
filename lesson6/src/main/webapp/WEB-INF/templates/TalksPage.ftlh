<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="talks" type="ru.itmo.wp.model.domain.Talk.TalkWithNames[]" -->
<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->

<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Message</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="sendMessage"/>
                <div class="field">
                    <div class="name">
                        <label for="selectUser">Login</label>
                    </div>
                    <div class="value">
                        <select id="selectUser" name="selectedUser">
                            <#list users as notThisUser>
                                <option>${notThisUser.login}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="text">${text!}</textarea>
                    </div>
                </div>
                <#if error??>
                    <div class="error">
                        ${error!}
                    </div>
                </#if>
                <div class="button-field">
                    <input type="submit" value="Send Message">
                </div>
            </form>
        </div>
    </div>

    <div class="datatable">
        <div class="caption">Messages</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Author</th>
                <th>Recipient</th>
                <th>Created</th>
                <th>Text</th>
            </tr>
            </thead>
            <tbody>
            <#list talks as talk>
                <tr>
                    <td>${talk.id}</td>
                    <td>${talk.sourceUserName}</td>
                    <td>${talk.targetUserName}</td>
                    <td>${talk.creationTime}</td>
                    <td>${talk.text}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>