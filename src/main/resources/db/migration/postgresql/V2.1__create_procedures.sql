DROP PROCEDURE IF EXISTS "delete_user";

-----------------------
-- procedure to delete an users with all of its dependencies
-----------------------

CREATE PROCEDURE "delete_user" (IN "user_id" character varying(255))
	LANGUAGE sql
	AS $$
		-- delete all of the user's information
		DELETE FROM "followers" WHERE "user_id" = "user_id";
		DELETE FROM "followers" WHERE "follower_id" = "user_id";
		DELETE FROM "files" WHERE "post_id" IN (SELECT "id" FROM "posts" WHERE "user_id" = "user_id");
		DELETE FROM "posts" WHERE "user_id" = "user_id";
	    DELETE FROM "rutinas" WHERE "user_id" = "user_id";
		DELETE FROM "users" WHERE "id" = "user_id";
	$$;