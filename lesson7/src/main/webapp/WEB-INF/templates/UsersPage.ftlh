<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <#if user?? && user.admin>
                    <th>Admin</th>
                </#if>
            </tr>
            </thead>
            <tbody>
                <template>
                    <tr>
                        <td class="user_id"></td>
                        <td class="user_login"></td>
                        <#if user?? && user.admin>
                            <td><a class="user_is_admin" href=""></a></td>
                        </#if>
                    </tr>
                </template>
                <tr class="noData">
                    <td colspan="3">
                        No data
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            const $noData = $(".users .noData");
            $noData.find("td").text("Loading...");

            ajax({
                    action: "findAll"
                },
                function (response) {
                    const thisUser = response["user"];
                    response["users"].forEach(function (user) {
                        $noData.hide();
                        const $tr = $($(".users template").prop("content")).clone();
                        $tr.find(".user_id").text(user["id"]);
                        $tr.find(".user_login").text(user["login"]);
                        if (thisUser && thisUser["admin"]) {
                            if (user["admin"]) {
                                $tr.find(".user_is_admin").text("disable");
                            } else {
                                $tr.find(".user_is_admin").text("enable");
                            }
                        }
                        $tr.find("tr").attr("data-userId", user["id"]);
                        $(".users tbody").append($tr);
                    });
                    $(".user_is_admin").click(function () {
                        const $href = $(this);
                        const $parentTr = $(this).parents(".users tr");
                        ajax({
                                action: "changeAdminValue",
                                adminCandidateId: $parentTr.attr("data-userId"),
                                isAdmin: ($href.text() === "enable")
                            },
                            function (response) {
                                if (response["user"]) {
                                    const newUser = response["user"];
                                    if (newUser["admin"]) {
                                        $href.text("disable");
                                    } else {
                                        $href.text("enable");
                                    }
                                }
                            }
                        );
                        return false;
                    });
                    $(".users tr").click(function () {
                        ajax({
                                action: "findUser",
                                userId: $(this).attr("data-userId")
                            },
                            function (response) {
                                notify(response["user"].creationTime);
                            }
                        )
                    })
                }
            );
        })
    </script>
</@c.page>
