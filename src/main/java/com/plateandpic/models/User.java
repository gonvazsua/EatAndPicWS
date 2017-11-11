package com.plateandpic.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "user")
public class User {
	
	@Id
    @Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "FIRSTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "TARGET", length = 150)
    @Size(min = 4, max = 150)
    private String target;	
    
    @Column(name = "ENABLED")
    @NotNull
    private Boolean enabled;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastPasswordResetDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;
    
    @Column(name = "picture")
    @JsonSerialize(using= com.plateandpic.serializer.ProfilePictureSerializer.class)
    private String picture;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;
  
    @JsonIgnore
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
  
    @NotNull
    private int rolId;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;
    
    @NotNull
    private Integer status;
    
    /*
     * FOREIGN KEY FOLLOWERS
     */
    @JsonIgnore
    @ManyToMany(targetEntity=User.class)
	private Set followers;
    
    @ManyToMany(targetEntity=PlatePicture.class)
	private Set likes;
    
    @ManyToOne
    private City city;
    
    
    /**
     * 
     */
    public User(){}    
    
    /**
     * @param email
     * @param name
     */
    public User(String email, String name){
    	this.email = email;
    	this.firstname = name;
    }
    
    /**
     * @param id
     */
    public User(Long id){
    	this.id = id;
    }
    
    /**
     * @param username
     * @param email
     * @param password
     */
    public User(String username, String email, String password){
    	this.username = username;
    	this.email = email;
    	this.password = password;
    	this.registerDate = new Date();
    	this.lastLogin = new Date();
    	this.rolId = Rol.USUARIO;
    }

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param userId
	 */
	public void setId(Long userId) {
		this.id = userId;
	}

	/**
	 * @return
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public Date getRegisterDate() {
		return registerDate;
	}

	/**
	 * @param registerDate
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	/**
	 * @return
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return
	 */
	public int getRolId() {
		return rolId;
	}

	/**
	 * @param rolId
	 */
	public void setRolId(int rolId) {
		this.rolId = rolId;
	}

	/**
	 * @return
	 */
	public Set getFollowers() {
		return followers;
	}

	/**
	 * @param followers
	 */
	public void setFollowers(Set followers) {
		this.followers = followers;
	}

	/**
	 * @return
	 */
	public Set getLikes() {
		return likes;
	}

	/**
	 * @param likes
	 */
	public void setLikes(Set likes) {
		this.likes = likes;
	}

	/**
	 * @return
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * @return
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return
	 */
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	/**
	 * @param lastPasswordResetDate
	 */
	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	/**
	 * @return
	 */
	public List<Authority> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities
	 */
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	/**
	 * @param otherUser
	 * @return
	 */
	public boolean equals(User otherUser) {
        if (this == otherUser)
            return true;
        if (id == null || otherUser == null || getClass() != otherUser.getClass())
            return false;
        
        return id.equals(otherUser.getId());
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
	
	/**
	 * @return
	 */
	public String getTarget(){
		return this.target;
	}
	
	/**
	 * @param target
	 */
	public void setTarget(String target){
		this.target = target;
	}

	/**
	 * @return
	 */
	public Restaurant getRestaurant() {
		return restaurant;
	}

	/**
	 * @param restaurant
	 */
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	/**
	 * @return
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * @return
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
