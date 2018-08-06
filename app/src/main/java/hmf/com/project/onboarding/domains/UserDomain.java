package hmf.com.project.onboarding.domains;

/**
 * Created by sai on 29-05-2017.
 */

public class UserDomain {
    private String type;

    // private UserOperationGroup userOperation;

    private String id;

    private String userName;

    private String location;

    private String firstName;

    private String lastName;

    private String emailId;

    private String phoneNumber;

    private String organization;

    // private String userRole;
    private boolean vendorProfileComleted;
    private String userObjectId;
    private String aboutTheOrganization;
    private String experience;
    private String logo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public boolean isVendorProfileComleted() {
        return vendorProfileComleted;
    }

    public void setVendorProfileComleted(boolean vendorProfileComleted) {
        this.vendorProfileComleted = vendorProfileComleted;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getAboutTheOrganization() {
        return aboutTheOrganization;
    }

    public void setAboutTheOrganization(String aboutTheOrganization) {
        this.aboutTheOrganization = aboutTheOrganization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSurveyorId() {
        return surveyorId;
    }

    public void setSurveyorId(String surveyorId) {
        this.surveyorId = surveyorId;
    }

    public String getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    private String tenantName;

    private String tenantId;

    private String surveyorId;

    private String badgeColor;

    private String userRole;
}
