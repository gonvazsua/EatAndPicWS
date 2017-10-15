package com.plateandpic.apiplace.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApiPlaceResponse {
	
	private List<String> html_attributions;
	private String next_page_token;
	private List<Result> results;
	
	@JsonIgnore
	private String status;
	
	public ApiPlaceResponse(){}

	/**
	 * @return the html_attributions
	 */
	public List<String> getHtml_attributions() {
		return html_attributions;
	}

	/**
	 * @param html_attributions the html_attributions to set
	 */
	public void setHtml_attributions(List<String> html_attributions) {
		this.html_attributions = html_attributions;
	}

	/**
	 * @return the next_page_token
	 */
	public String getNext_page_token() {
		return next_page_token;
	}

	/**
	 * @param next_page_token the next_page_token to set
	 */
	public void setNext_page_token(String next_page_token) {
		this.next_page_token = next_page_token;
	}

	/**
	 * @return the results
	 */
	public List<Result> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<Result> results) {
		this.results = results;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
