package ru.itmo.web.lesson4.util;

import ru.itmo.web.lesson4.model.Post;
import ru.itmo.web.lesson4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", User.UserColor.GREEN),
            new User(6, "pashka", "Pavel Mavrin", User.UserColor.GREEN),
            new User(9, "geranazarov555", "Georgiy Nazarov", User.UserColor.RED),
            new User(11, "tourist", "Gennady Korotkevich", User.UserColor.BLUE)
    );
    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, 11, "First Post", "Lorem ipsum dolor."),
            new Post(3, 1, "Second Post", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur, nostrum!"),
            new Post(4, 6, "Post 3", "Lorem ipsum dolor."),
            new Post(10, 9, "Post 4", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus accusantium atque debitis doloremque, eos expedita fugit hic inventore ipsa iste labore laborum natus nobis odio placeat possimus provident quis recusandae rem similique sint sit sunt velit? Aut doloremque eaque minima odio qui sed suscipit velit! Adipisci aspernatur beatae ea, eius fugiat inventore iste nesciunt, non odio omnis porro praesentium quas repellendus reprehenderit saepe tempore unde velit voluptatibus? Amet, consectetur dolores esse modi quam reiciendis sapiente sit tempora vel vitae. Accusantium aut consectetur deserunt ea error explicabo, libero nam obcaecati quis veritatis? Aperiam consectetur consequuntur corporis dolore esse et, eveniet harum inventore iusto minima nesciunt nihil obcaecati odit possimus quae quia similique? Aperiam fugit in maxime perferendis, quod ratione reiciendis reprehenderit soluta velit voluptas? Accusamus accusantium dolorem ducimus expedita illo impedit ipsam iste laudantium maxime minima natus nemo nobis obcaecati perspiciatis placeat quis quisquam reiciendis repellat, reprehenderit saepe sint soluta tempora tempore temporibus unde vero voluptatibus voluptatum? Accusantium consectetur itaque obcaecati odio possimus quia tempora totam. Alias atque aut consequuntur delectus dolore dolores ea eaque, eos expedita in labore magnam maxime mollitia non porro sunt, tenetur voluptate voluptates! Aperiam blanditiis, dolor excepturi facere ipsam quibusdam tempora. Adipisci cum cumque dignissimos distinctio earum facilis hic, laboriosam libero maiores modi nam neque nostrum odio porro repellat tenetur velit, vero voluptatibus. Alias aliquam consequatur culpa cupiditate deleniti dolore doloribus earum eligendi, hic impedit in nesciunt possimus quas qui quia quibusdam quidem voluptate? Consequatur dolor doloremque dolorum maxime rerum suscipit ut vero. Architecto assumenda, ducimus esse et fugiat id magnam, nemo nostrum numquam odit perferendis, perspiciatis praesentium rerum sed ullam vel vero voluptatum? Dignissimos ea eaque eveniet, exercitationem facere illum natus nesciunt nulla perferendis placeat praesentium quo quos recusandae saepe sed! Aut culpa dignissimos iure, labore modi molestias nostrum nulla similique sit? Error, obcaecati, ut?"),
            new Post(12, 6, "Post 5", "Lorem ipsum dolor."),
            new Post(111, 11, "Post 6", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium adipisci animi blanditiis consequuntur delectus deleniti dolore dolorem ducimus eaque error est ex explicabo, facere fuga hic laboriosam maiores minima nisi nobis non omnis possimus quae recusandae repellendus sit suscipit voluptas voluptates? Dignissimos illum laboriosam, quam sequi vitae voluptates. Alias amet dignissimos ducimus harum magni nesciunt quis quo ullam vitae voluptas. Cupiditate dolore ducimus enim et facilis fuga illo impedit modi nam neque non numquam, placeat porro provident quae quis recusandae repellat temporibus voluptas voluptatem? Ad amet, at beatae ex facilis, fugiat in inventore ipsam non nulla numquam, officiis possimus recusandae. Ab architecto eligendi, eum ex natus quae velit! Aliquid dolore esse eum exercitationem magnam natus nostrum quo quos sed, tenetur. Adipisci beatae, blanditiis earum exercitationem expedita facilis, iure labore nihil non perferendis porro provident quasi quidem quisquam quos saepe sunt? Aut, debitis eius error ex expedita fuga hic ipsum itaque laborum magnam mollitia nemo nisi quasi, reprehenderit sequi sit tempore. A animi consectetur dolorem enim ex fugit magnam minus modi nam, numquam omnis praesentium provident tempora voluptate voluptatibus. Ab cupiditate ducimus, esse nemo quae sequi voluptas. Animi culpa, debitis dolorem dolores facilis incidunt, laborum odit omnis quibusdam quisquam tenetur totam!")
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
