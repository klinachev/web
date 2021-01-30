<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="comment" type="ru.itmo.wp.domain.Comment" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if post??>
        <@c.post macroPost=post/>
        <#if user??>
            <div class="comment form-box">
                <div class="header">Write Comment</div>
                <div class="body">
                    <form method="post" action="">
                        <input type="hidden" name="action" value="writeComment"/>
                        <div class="field">
                            <div class="name">
                                <label for="text">Text</label>
                            </div>
                            <div class="value">
                                <textarea id="text" name="text">${comment.text!}</textarea>
                            </div>
                            <@c.error "comment.text"/>
                        </div>
                        <div class="button-field">
                            <input type="submit" value="Write">
                        </div>
                    </form>
                </div>
            </div>
        </#if>
        <div class="users datatable">
            <div class="caption">Comments</div>
            <table>
                <thead>
                <tr>
                    <th>User</th>
                    <th>Comment</th>
                </tr>
                </thead>
                <tbody>
                <#if post.comments?size==0>
                    <tr class="noData">
                        <td colspan="2">
                            No data
                        </td>
                    </tr>
                <#else>
                    <#list post.comments as listComment>
                        <tr>
                            <td class="id">${listComment.user.login}</td>
                            <td class="login">${listComment.text}</td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    <#else>
        No such post
    </#if>
</@c.page>
