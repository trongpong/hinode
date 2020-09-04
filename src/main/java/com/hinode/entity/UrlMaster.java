package com.hinode.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "urlMaster")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UrlMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "page_name")
	private String pageName;

	@Column(name = "link_login")
	private String linkLogin;

	@Column(name = "link_search_data", columnDefinition = "text")
	private String linkSearchData;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "pass_word")
	private String passWord;
}
