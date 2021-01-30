<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">Article</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <#list articles as article>
            <tr>
                <th>${article.id}</th>
                <th>${article.title}</th>
                <th class="changeHidden">
                    <form name="changeHidden" method="post" action="">
                    <input type="hidden" name="action" value="${article.id}"/>
                    <input name="changeButton" type="submit" value=
                    <#if article.hidden>
                        "Show"
                    <#else>
                        "Hide"
                    </#if>
                    >
                    </form>
                </th>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            $(".changeHidden form").submit(function () {
                const id = $(this).find("input[name='action']").val();
                const $button = $(this).find("input[name='changeButton']");

                ajax({
                        action: "changeArticleHidden",
                        id: id,
                        newHidden: ($button.val() === "Hide")
                    },
                    function (response) {
                        const article = response["article"];
                        if (article["hidden"]) {
                            $button.val("Show");
                        } else {
                            $button.val("Hide");
                        }
                    }
                );
                return false;
            })
        })
    </script>
</@c.page>
