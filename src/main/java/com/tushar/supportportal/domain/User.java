package com.tushar.supportportal.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "users")
public record User(
		@NonNull
		@Id
		@Field(name = "_id", write = Field.Write.NON_NULL)
		// makes it non null and mongo automatically ensures _id -> non updatatable
		String _id,
		String userId,
		String username,
		String password,
		String firstName,
		String lastName,
		String email,
		String profileImageUrl,
		Date lastLoginDate,
		Date lastLoginDateDisplay,
		Date joinedDate,
		String[] roles,
		// roles with authorities -> ROLE_USER(user:delete,user:read,user:self), ROLE_ADMIN (user:delete)
		String[] authorities,
		Boolean isActive,
		Boolean isLocked
) implements Serializable {
}
