Setup instructions:

1. Development done using Core Java and Build using Maven, overall its command line project.
2. Fork the project from https://github.com/Guru-kn/java-projects/tree/master/deliverysolution
3. mvn clean install to install all the required dependencies as in pom.xml file.
4. We have two problems discount calculation, delivery solution, so i have created 2 main classes.
5. 1st main class is DiscountMainApp.java and 2nd is DeliveryMainApp.java for discount and delivery solution respectively.
6. Both files take same 2 sets of input:
   . 1st is basedeliverycost and number of packages ie 100 3
   . 2nd set of input is entering all 3 package details as follows:
   PKG1 5 5 OFR001
   PKG2 15 5 OFR002
   PKG3 10 100 OFR003
7. I am logging output as:
    PKG1 0.0 175.0
    PKG2 0.0 275.0
    PKG3 35.0 665.0
