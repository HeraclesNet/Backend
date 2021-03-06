DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS "files" CASCADE;
DROP TABLE IF EXISTS "posts" CASCADE;
DROP TABLE IF EXISTS "rutinas" CASCADE;
DROP TABLE IF EXISTS "followers" CASCADE;
DROP TABLE IF EXISTS "posts_muscles" CASCADE;

-----------------------
-- Table: users
-----------------------

CREATE TABLE "public"."users" (
	"id" character varying(255) NOT NULL DEFAULT uuid_generate_v4()::text,
	"data_of_birth" date  NOT NULL,
	"email" character varying(255) NOT NULL,
	"gender" boolean,
	"height" real,
	"name" TEXT NOT NULL,
	"nick_name" character varying(255) NOT NULL, 
	"password" TEXT NOT NULL,
	"visibility" boolean NOT NULL,
	"weight" real,
	CONSTRAINT "users_pkey" PRIMARY KEY ("id"),
	CONSTRAINT "users_email_unique" UNIQUE ("email"),
	CONSTRAINT "users_nick_name_unique" UNIQUE ("nick_name")
);

-----------------------
-- Table: posts
-----------------------

CREATE TABLE "public"."posts" (
	"id" character varying(255) NOT NULL DEFAULT uuid_generate_v4()::text,
	"content" TEXT NOT NULL,
	"muscles" INTEGER,
	"created_at" TIMESTAMP NOT NULL,
	"user_id" character varying(255) NOT NULL,
	CONSTRAINT "posts_pkey" PRIMARY KEY ("id"),
	CONSTRAINT "fk_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id")
);

-----------------------
-- Table: files
-----------------------

CREATE TABLE "public"."files" (
	"id" character varying(255) NOT NULL DEFAULT uuid_generate_v4()::text,
	"content" oid NOT NULL,
	"name" character varying(255) NOT NULL,
	"type" character varying(255) NOT NULL,
	"post_id" character varying(255) NOT NULL,
	CONSTRAINT "files_pkey" PRIMARY KEY ("id"),
	CONSTRAINT "fk_post_id" FOREIGN KEY ("post_id") REFERENCES "posts" ("id")
);

-----------------------
-- Table: followers
-----------------------

CREATE TABLE "public"."followers" (
	"id" character varying(255) NOT NULL DEFAULT uuid_generate_v4()::text,
	"user_id" character varying(255) NOT NULL,
	"follower_id" character varying(255) NOT NULL,
	CONSTRAINT "followers_pkey" PRIMARY KEY ("id"),
	CONSTRAINT "fk_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id"),
	CONSTRAINT "fk_follower_id" FOREIGN KEY ("follower_id") REFERENCES "users" ("id")
);

-----------------------
-- Table: rutinas
-----------------------

CREATE TABLE "public"."rutinas"  (
	"id" character varying(255) NOT NULL DEFAULT uuid_generate_v4()::text,
	"date" character varying(255) NOT NULL,
	"user_id" character varying(255) NOT NULL,
	"startt" character varying(255) NOT NULL,
	"ent" character varying(255) NOT NULL,
	"text" TEXT,
	CONSTRAINT "idPK" PRIMARY KEY ("id"),
	CONSTRAINT "fk_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id")
);

-----------------------
-- Table: posts_muscles
-----------------------

CREATE TABLE "public"."posts_muscles" (
	"id" character varying(255) NOT NULL DEFAULT uuid_generate_v4()::text,
	"post_id" character varying(255) NOT NULL,
	"user_id" character varying(255) NOT NULL,
	CONSTRAINT "posts_muscles_key" PRIMARY KEY ("id"),
	CONSTRAINT "fk_post_id" FOREIGN KEY ("post_id") REFERENCES "posts" ("id"),
	CONSTRAINT "fk_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id")
);