<template>
    <div>
<!--        <div>-->
<!--            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium-->
<!--            alias architecto beatae commodi consequuntur distinctio doloremque,-->
<!--            ea eligendi eveniet, exercitationem fuga id maiores minima modi nemo-->
<!--            neque nesciunt non nostrum officiis optio quaerat quas recusandae-->
<!--            reiciendis saepe temporibus. At deserunt quis repudiandae sapiente voluptatum.-->
<!--            Ea error maiores nulla ratione vitae. Amet aperiam consequuntur corporis cum-->
<!--            cumque delectus dicta dolor doloribus eaque eligendi eos esse et-->
<!--            expedita explicabo fugit hic impedit in ipsa labore laudantium modi molestias nisi numquam-->
<!--            omnis quaerat quam quidem quis quo repellendus repudiandae similique soluta, tempora ut vel-->
<!--            veritatis vitae voluptatum! Aliquid deserunt doloremque et minus soluta?-->
<!--        </div>-->
        <article v-for="post in posts" :post="post" :key="post.id">
            <div class="title">{{ post.title }}</div>

            <div class="information">User id: {{ post.userId}}</div>
            <div class="body">{{ post.text }}</div>
            <ul class="attachment">
                <li>Announcement of <a href="#">Codeforces Round #510 (Div. 1)</a></li>
                <li>Announcement of <a href="#">Codeforces Round #510 (Div. 2)</a></li>
            </ul>
            <div class="footer">
                <div class="left">
                    <img src="../../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
                    <span class="positive-score">+173</span>
                    <img src="../../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
                </div>
                <div class="right">
                    <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                    <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
                    {{ commentsCounts[post.id] }}
                </div>
            </div>
        </article>
    </div>

</template>

<script>
    export default {
        name: "Index",
        props: ["posts", "commentsCounts"]
    }
</script>

<style scoped>

</style>