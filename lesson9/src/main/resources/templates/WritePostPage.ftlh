<#-- @ftlvariable name="postFields" type="ru.itmo.wp.form.PostFields" -->

<#import "/spring.ftl" as spring>
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form">
        <div class="header">Write Post</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="writePost"/>
                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input autofocus id="title" name="title" value="${postFields.title!}"/>
                    </div>
                    <@c.error "postFields.title"/>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="tags">Tags</label>
                    </div>
                    <div class="value">
                        <input autofocus id="tags" name="tags" value="${postFields.tags!}"/>
                    </div>
                    <@c.error "postFields.tags"/>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="text">${postFields.text!}</textarea>
                    </div>
                    <@c.error "postFields.text"/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Write">
                </div>
            </form>
        </div>
    </div>
</@c.page>
