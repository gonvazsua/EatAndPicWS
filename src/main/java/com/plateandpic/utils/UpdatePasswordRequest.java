package com.plateandpic.utils;

/**
 * @author gonzalo
 *
 */
public class UpdatePasswordRequest {
	
	private String lastPassword;
	private String newPassword1;
	private String newPassword2;
	
	/**
	 * @return
	 */
	public String getLastPassword() {
		return lastPassword;
	}
	/**
	 * @param lastPassword
	 */
	public void setLastPassword(String lastPassword) {
		this.lastPassword = lastPassword;
	}
	/**
	 * @return
	 */
	public String getNewPassword1() {
		return newPassword1;
	}
	/**
	 * @param newPassword1
	 */
	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}
	/**
	 * @return
	 */
	public String getNewPassword2() {
		return newPassword2;
	}
	/**
	 * @param newPassword2
	 */
	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}
	
	

}
