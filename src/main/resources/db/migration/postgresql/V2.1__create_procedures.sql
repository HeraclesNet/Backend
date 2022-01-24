DROP PROCEDURE IF EXISTS "delete_user";

-----------------------
-- procedure to delete an users with all of its dependencies
-----------------------

CREATE PROCEDURE "delete_user" (IN "id_user" character varying(255))
	LANGUAGE sql
	AS $$
		-- delete all of the user's information
		DELETE FROM "followers" WHERE "user_id" = "id_user";
		DELETE FROM "followers" WHERE "follower_id" = "id_user";
		DELETE FROM "files" WHERE "post_id" IN (SELECT "id" FROM "posts" WHERE "user_id" = "id_user");
		DELETE FROM "posts_muscles" WHERE "user_id" = "id_user";
		DELETE FROM "posts" WHERE "user_id" = "id_user";
	    DELETE FROM "rutinas" WHERE "user_id" = "id_user";
		DELETE FROM "users" WHERE "id" = "id_user";
	$$;