package com.muizz.spring.jooq.utils.resource;

import java.util.Optional;

public class ResourcePage {

    private int number;
	private int offset;
	private Long seekId; // The ID which is used as a seek predicate for the Seek pagination method
	private int limit;
	private int size;

	private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;


	public ResourcePage() {

		// Set default page number and size if not set
		this.number = ((number != 0) ? number : DEFAULT_PAGE_NUMBER);
		this.size = ((size != 0) ? size : DEFAULT_PAGE_SIZE);

		// Calculate page limit and offset
		offset = Math.max(0, (this.number - 1) * limit);
		limit = Math.max(1, this.size);

	}


	public ResourcePage(
		int number,
		int offset,
		Long seekId,
		int limit,
		int size
	) {
		this.number = number;
		this.offset = offset;
		this.seekId = seekId;
		this.limit = limit;
		this.size = size;
	}


	public Optional<Long> getSeekIdOptional() {
		return Optional.ofNullable(seekId);
	}

	public Optional<Integer> getOffsetOptional() {
		return Optional.ofNullable(offset);
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public void setSeekId(Long seekId) {
		this.seekId = seekId;
	}

	public Long getSeekId() {
		return seekId;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

}
