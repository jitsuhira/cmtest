package jp.classmethod.testing.examples.verifier;

import java.util.Date;

public class Item {
	Long id;
	String name;
	Integer price;
	String description;
	Date createdAt;

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price
				+ ", description=" + description + ", createdAt=" + createdAt
				+ "]";
	}

}
