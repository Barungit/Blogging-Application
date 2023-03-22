package com.elearn.blog.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="blog")
@NoArgsConstructor
@Getter
@Setter
public class Blog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bid;
	
	@Column(length = 100, nullable = false)
	private String title;
	@Column(length = 30000, nullable = false)
	private String content;
	private String picname;
	private Date uploadDate;
	
	@ManyToOne
	@JoinColumn(name = "cid")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "uid")
	private User user;
	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<>();
}
