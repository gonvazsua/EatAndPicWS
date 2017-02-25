package com.eatandpic.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.eatandpic.crypt.CryptoConverter;

@Entity
@Table(name = "user")
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Null
    private String name;
  
    @Null
    private String surname;
  
    @NotNull
    private String username;
  
    @NotNull
    private String email;
  
    @NotNull
    private String password;
    
    @Null
    private String picture;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;
  
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
  
    @NotNull
    private int rolId;
    
    /*
     * FOREIGN KEY FOLLOWERS
     */
    @ManyToMany(targetEntity=User.class)
	private Set followers;
    
    @ManyToMany(targetEntity=Picture.class)
	private Set likes;
    
    
    public User(){}    
    
    public User(String email, String name){
    	this.email = email;
    	this.name = name;
    }
    
    public User(long id){
    	this.userId = id;
    }
    
    public User(String username, String email, String password){
    	this.username = username;
    	this.email = email;
    	this.password = password;
    	this.registerDate = new Date();
    	this.lastLogin = new Date();
    	this.rolId = Rol.USUARIO;
    }

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getRolId() {
		return rolId;
	}

	public void setRolId(int rolId) {
		this.rolId = rolId;
	}

	public Set getFollowers() {
		return followers;
	}

	public void setFollowers(Set followers) {
		this.followers = followers;
	}

	public Set getLikes() {
		return likes;
	}

	public void setLikes(Set likes) {
		this.likes = likes;
	}
	
	public void setCryptedPassword(String nonCryptedPassword){
		this.setPassword(new CryptoConverter().convertToDatabaseColumn(nonCryptedPassword));
	}
	
	public void cryptPassword(){
		this.setCryptedPassword(this.getPassword());
	}
	
	public String cryptPassword(String nonCryptedPassword){
		String crypted = (new CryptoConverter().convertToDatabaseColumn(nonCryptedPassword));
		return crypted;
	}
	
	public void prepareForRegister(){
		this.username = this.getUsername();
    	this.email = this.getEmail();
    	this.setCryptedPassword(this.getPassword());
    	this.registerDate = new Date();
    	this.lastLogin = new Date();
    	this.rolId = Rol.USUARIO;
	}
	
	public boolean checkPassword(String passwordToCheck){
		String crypted = cryptPassword(passwordToCheck);
		
		if(crypted.equals(this.getPassword())){
			return true;
		}
		
		return false;
	}
}
