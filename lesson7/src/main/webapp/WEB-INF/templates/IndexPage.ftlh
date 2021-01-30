<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <template>
            <article>
                <div class="title"></div>
                <div class="information"></div>
                <div class="body"></div>
            </article>
        </template>
        <div class="noData">
            Welcome on Codeforces!
        </div>
    </div>
<script>
    $(function () {
        const $noData = $(".noData");
        $noData.text("Loading...");

        ajax(
            {
                action: "findAllUnhiddenArticles"
            },
            function (response) {
                response["articles"].forEach(function (article) {
                        $noData.hide();
                        const $articleBlock = $($(".articles template").prop("content")).clone();
                        $articleBlock.find(".title").text(article["title"]);
                        $articleBlock.find(".information").text("By " + article["userName"] + " at " + article["creationTime"]);
                        $articleBlock.find(".body").text(article["text"]);

                        $(".articles").append($articleBlock);
                })
            }
        );
    });
    $(function () {
        $("aside section").click(function () {
            $(this).css("background-color", "red");
        });
    })
</script>
</@c.page>
