//package org.codeforamerica.formflowstarter.testutilities;
//
//
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import org.codeforamerica.formflowstarter.app.data.ApplicationData;
//import org.codeforamerica.formflowstarter.app.data.Submission;
//
///**
// * Helper class for building test submissions
// */
//public class TestSubmissionBuilder {
//
//  private final Submission submission;
//
//  public TestSubmissionBuilder() {
//    submission = new Submission();
//  }
//
//  public TestSubmissionBuilder(Submission submission) {
//    this.submission = submission;
//  }
//
//  public Submission build() {
//    return submission;
//  }
//
//  public TestSubmissionBuilder base() {
//    submission.setId("20");
//    submission.setStartTimeOnce(Instant.now());
//    return this;
//  }
//
//  public TestSubmissionBuilder withApplicantPrograms(List<String> programs) {
//    PageData programPage = new PageData();
//    programPage.put("programs", new InputData(programs));
//    applicationData.getPagesData().put("choosePrograms", programPage);
//    return this;
//  }
//
//  public TestSubmissionBuilder withPersonalInfo() {
//    PageData personalInfo = getPageData("personalInfo");
//    personalInfo.put("firstName", new InputData(List.of("Jane")));
//    personalInfo.put("lastName", new InputData(List.of("Doe")));
//    personalInfo.put("otherName", new InputData(List.of("")));
//    personalInfo.put("dateOfBirth", new InputData(List.of("10", "04", "2020")));
//    personalInfo.put("ssn", new InputData(List.of("123-45-6789")));
//    personalInfo.put("sex", new InputData(List.of("FEMALE")));
//    personalInfo.put("maritalStatus", new InputData(List.of("NEVER_MARRIED")));
//    personalInfo.put("livedInMnWholeLife", new InputData(List.of("true")));
//    return this;
//  }
//
//  public TestSubmissionBuilder withContactInfo() {
//    PageData pageData = getPageData("contactInfo");
//    pageData.put("phoneNumber", new InputData(List.of("(603) 879-1111")));
//    pageData.put("email", new InputData(List.of("jane@example.com")));
//    pageData.put("phoneOrEmail", new InputData(List.of("PHONE")));
//    return this;
//  }
//
//  public TestSubmissionBuilder noPermanentAddress() {
//    PageData pageData = getPageData("homeAddress");
//    pageData.put("isHomeless", new InputData(List.of("true")));
//    return this;
//  }
//
//  public TestSubmissionBuilder withHomeAddress() {
//    PageData pageData = getPageData("homeAddress");
//    pageData.put("streetAddress", new InputData(List.of("street")));
//    pageData.put("city", new InputData(List.of("city")));
//    pageData.put("state", new InputData(List.of("CA")));
//    pageData.put("zipCode", new InputData(List.of("02103")));
//    pageData.put("apartmentNumber", new InputData(List.of("ste 123")));
//    return this;
//  }
//
//  public TestSubmissionBuilder withEnrichedHomeAddress() {
//    PageData pageData = getPageData("homeAddress");
//    pageData
//        .put("enrichedStreetAddress", new InputData(List.of("smarty street")));
//    pageData.put("enrichedCity", new InputData(List.of("smarty city")));
//    pageData.put("enrichedState", new InputData(List.of("CA")));
//    pageData.put("enrichedZipCode", new InputData(List.of("02103-9999")));
//    pageData.put("enrichedApartmentNumber", new InputData(List.of("apt 123")));
//    return this;
//  }
//
//  public TestSubmissionBuilder withMailingAddress() {
//    PageData pageData = getPageData("mailingAddress");
//    pageData.put("streetAddress", new InputData(List.of("street")));
//    pageData.put("city", new InputData(List.of("city")));
//    pageData.put("state", new InputData(List.of("CA")));
//    pageData.put("zipCode", new InputData(List.of("02103")));
//    pageData.put("apartmentNumber", new InputData(List.of("ste 123")));
//    return this;
//  }
//
//  public TestSubmissionBuilder withEnrichedMailingAddress() {
//    PageData pageData = getPageData("mailingAddress");
//    pageData.put("enrichedStreetAddress", new InputData(List.of("smarty street")));
//    pageData.put("enrichedCity", new InputData(List.of("smarty city")));
//    pageData.put("enrichedState", new InputData(List.of("CA")));
//    pageData.put("enrichedZipCode", new InputData(List.of("02103-9999")));
//    pageData.put("enrichedApartmentNumber", new InputData(List.of("apt 123")));
//    return this;
//  }
//
//  public TestSubmissionBuilder withPageData(String pageName, String input, String value) {
//    return withPageData(pageName, input, List.of(value));
//  }
//
//  public TestSubmissionBuilder withPageData(String pageName, String input,
//      List<String> values) {
//    PageData pageData = getPageData(pageName);
//    pageData.put(input, new InputData(values));
//    return this;
//  }
//
//  public TestSubmissionBuilder withSubworkflow(String pageGroup, PagesData... pagesData) {
//    applicationData.setSubworkflows(
//        new Subworkflows(Map.of(pageGroup, new Subworkflow(Arrays.asList(pagesData)))));
//    return this;
//  }
//
//  // Will overwrite existing subworkflows data
//  public TestSubmissionBuilder withSubworkflow(String pageGroup,
//      PagesDataBuilder... pagesDataBuilder) {
//    List<PagesData> pagesDataList = Arrays.stream(pagesDataBuilder)
//        .map(PagesDataBuilder::build).toList();
//    applicationData.getSubworkflows().put(pageGroup, new Subworkflow(pagesDataList));
//    return this;
//  }
//
//  public TestSubmissionBuilder withJobs() {
//    return withSubworkflow("jobs", new PagesDataBuilder()
//        .withNonHourlyJob("false", "1.1", "EVERY_WEEK"));
//  }
//
//  public TestSubmissionBuilder withHouseholdMemberPrograms(List<String> programs) {
//    return withSubworkflow("household", new PagesDataBuilder()
//        .withPageData("householdMemberInfo", "programs", programs)
//    );
//  }
//
//  /**
//   * Gets the PageData for the given pageName - if it doesn't exist, add it and return the new
//   * PageData object.
//   */
//  private PageData getPageData(String pageName) {
//    applicationData.getPagesData().putIfAbsent(pageName, new PageData());
//    return applicationData.getPagesData().get(pageName);
//  }
//
//  public ApplicationData withUploadedDocs() {
//    applicationData.setUploadedDocs(List.of(
//        new UploadedDocument("doc1", "", "", "", 1000),
//        new UploadedDocument("doc2", "", "", "", 2000)
//    ));
//    return applicationData;
//  }
//
//  public TestSubmissionBuilder withHouseholdMember(String firstName, String lastName) {
//    return withSubworkflow("household", new PagesDataBuilder()
//        .withPageData("householdMemberInfo",
//            Map.of("firstName", firstName,
//                "lastName", lastName,
//                "dateOfBirth", List.of("5", "6", "1978"),
//                "maritalStatus", "Never married",
//                "sex", "Female",
//                "livedInMnWholeLife", "Yes",
//                "relationship", "housemate",
//                "programs", "SNAP",
//                "ssn", "123121234")));
//  }
//
//  public TestSubmissionBuilder withMultipleHouseholdMembers() {
//    return withSubworkflow("household",
//        new PagesDataBuilder().withPageData("householdMemberInfo",
//            Map.of("firstName", "Other",
//                "lastName", "Person",
//                "dateOfBirth", List.of("5", "6", "1978"),
//                "maritalStatus", "Never married",
//                "sex", "Female",
//                "livedInMnWholeLife", "Yes",
//                "relationship", "housemate",
//                "programs", "SNAP",
//                "ssn", "123121234")),
//        new PagesDataBuilder().withPageData("householdMemberInfo",
//            Map.of("firstName", "Daria",
//                "lastName", "Agàta",
//                "dateOfBirth", List.of("5", "6", "1978"),
//                "maritalStatus", "Never married",
//                "sex", "Female",
//                "livedInMnWholeLife", "Yes",
//                "relationship", "spouse",
//                "programs", "SNAP",
//                "ssn", "123121235")));
//  }
//}
