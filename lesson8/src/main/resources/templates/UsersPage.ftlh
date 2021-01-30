<#-- @ftlvariable name="users" type="ru.itmo.wp.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
                <#if user??>
                    <th>Status</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#if users?size==0>
                <tr class="noData">
                    <td colspan="3">
                        No data
                    </td>
                </tr>
            <#else>
                <#list users as listUser>
                    <tr>
                        <td class="id">${listUser.id}</td>
                        <td class="login">${listUser.login}</td>
                        <td class="creationTime">${listUser.creationTime}</td>
                        <#if user??>
                            <td class="disabled">
                                <#if listUser.disabled>
                                    <a href="/users/enable${listUser.id}">enable</a>
                                <#else>
                                    <a href="/users/disable${listUser.id}">disable</a>
                                </#if>
                            </td>
                        </#if>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>
