# 面向服务的软件工程 Assignment One

## 201250182 郑义

## 校档案馆档案管理

### Schema 设计

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:am="archive-management"
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="archive-management">

    <xs:simpleType name="gender" final="restriction">
        <xs:restriction base="xs:string">
            <xs:enumeration value="male"/>
            <xs:enumeration value="man"/>
        </xs:restriction>
    </xs:simpleType>

    <xs:complexType name="studentInfo">
        <xs:sequence>
            <xs:element name="id" type="xs:string"/>
            <xs:element name="name" type="xs:string"/>
            <xs:element name="age" type="xs:int"/>
            <xs:element name="gender" type="am:gender"/>
            <xs:element name="major" type="xs:string"/>
            <xs:element name="enrollYear" type="xs:int"/>
        </xs:sequence>
    </xs:complexType>

    <xs:element name="studentInfo" type="am:studentInfo"/>
</xs:schema>
```

### XML 实例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<am:studentInfo xmlns:am="archive-management">
    <id>201250001</id>
    <name>Bond</name>
    <age>35</age>
    <gender>man</gender>
    <major>Computer Science</major>
    <enrollYear>2020</enrollYear>
</am:studentInfo>
```

## 上课签到

### Schema 设计

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:si="sign-in"
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="sign-in">

    <xs:complexType name="studentInfo">
        <xs:sequence>
            <xs:element name="id" type="xs:string"/>
            <xs:element name="name" type="xs:string"/>
            <xs:element name="className" type="xs:string"/>
            <xs:element name="signInTime" type="xs:dateTime"/>
        </xs:sequence>
    </xs:complexType>

    <xs:element name="student" type="si:studentInfo"/>
</xs:schema>
```

### XML 实例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<si:student xmlns:si="sign-in"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="StudentInfo.xsd">
    <id>201250001</id>
    <name>Bond</name>
    <className>Machine Learning</className>
    <signInTime>2023-11-14T13:58:00</signInTime>
</si:student>
```

## XML Processing(DOM)

### 以学生为聚合的成绩报告单 Schema

`CourseInfo.xsd`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="educational-admin">
    <xs:complexType name="courseInfo">
        <xs:sequence>
            <xs:element name="courseName" type="xs:string"/>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

`Grade.xsd`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="educational-admin">
    <xs:complexType name="grade">
        <xs:sequence>
            <xs:element name="usualGrade" type="xs:decimal"/>
            <xs:element name="finalGrade" type="xs:decimal"/>
            <xs:element name="totalGrade" type="xs:decimal"/>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

`StudentGrades.xsd`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:ea="educational-admin"
           xmlns:am="archive-management"
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="educational-admin">

    <xs:import namespace="archive-management" schemaLocation="../archive-management/StudentInfo.xsd"/>
    <xs:include schemaLocation="CourseInfo.xsd"/>
    <xs:include schemaLocation="Grade.xsd"/>

    <xs:element name="studentGradeList">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="studentGrade" type="ea:studentGrade" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:complexType name="studentGrade">
        <xs:sequence>
            <xs:element name="studentInfo" type="am:studentInfo"/>
            <xs:element name="courseGrades" type="ea:studentCourseGrade"/>
        </xs:sequence>
    </xs:complexType>

    <xs:complexType name="studentCourseGrade">
        <xs:sequence>
            <xs:element name="courseInfo" type="ea:courseInfo"/>
            <xs:element name="grade" type="ea:grade" maxOccurs="unbounded"/>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

### 脚本处理

```java
public class EducationalAdminServiceImpl implements EducationalAdminService {

    private final ArchiveManagementService archiveManagementService;

    public EducationalAdminServiceImpl(ArchiveManagementService archiveManagementService) {
        this.archiveManagementService = archiveManagementService;
    }
    
    /**
     * 处理学生成绩单
     */
    @Override
    public void handleStudentGrade() {
        // 获取学生信息
        StudentInfo studentInfo = archiveManagementService.getStudentInfos().get(0);
        // 生成学生信息
        List<StudentInfo> studentInfos = DataUtils.generateStudentInfos(9);
        studentInfos.add(studentInfo);
        // 生成成绩
        List<StudentGrade> studentGrades = DataUtils.generateStudentGrades(studentInfos);
        // 转化为 XML 文档
        DocumentUtils.toXML(new StudentGradeList(studentGrades),
                "src/main/resources/educational-admin/StudentGrades.xml", StudentGradeList.class);
    }
}
```

使用 DOM 读取 XML 文件

```java
public class ArchiveManagementServiceImpl implements ArchiveManagementService {

    private static final String RESOURCES_PATH = "/archive-management/";
    private static final String XML_FILE = RESOURCES_PATH + "StudentInfo.xml";
    private static final String SCHEMA_FILE = RESOURCES_PATH + "StudentInfo.xsd";

    @Override
    public List<StudentInfo> getStudentInfos() {
        String documentPath = "src/main/resources" + XML_FILE;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(documentPath);
            return readContentFromDocument(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<StudentInfo> readContentFromDocument(Document document) {
        List<StudentInfo> contentList = new ArrayList<>();
        NodeList nodelist = document.getElementsByTagName("am:studentInfo");
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                StudentInfo studentInfo = new StudentInfo();
                studentInfo.setId(element.getElementsByTagName("id").item(0).getTextContent());
                studentInfo.setName(element.getElementsByTagName("name").item(0).getTextContent());
                studentInfo.setAge(Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent()));
                studentInfo.setMajor(element.getElementsByTagName("major").item(0).getTextContent());
                studentInfo.setGender(element.getElementsByTagName("gender").item(0).getTextContent());
                studentInfo.setEnrollYear(Integer.parseInt(element.getElementsByTagName("enrollYear").item(0).getTextContent()));
                contentList.add(studentInfo);
            }
        }

        return contentList;
    }
}
```

数据生成可以查看 `DataUtils.java`

### 结果展示

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ea:studentGradeList xmlns:ea="educational-admin">
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>12.9</finalGrade>
                <totalGrade>13.6</totalGrade>
                <usualGrade>0.7</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>54.0</finalGrade>
                <totalGrade>54.1</totalGrade>
                <usualGrade>0.1</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>52.8</finalGrade>
                <totalGrade>82.4</totalGrade>
                <usualGrade>29.6</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>45.4</finalGrade>
                <totalGrade>76.8</totalGrade>
                <usualGrade>31.4</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>9.7</finalGrade>
                <totalGrade>30.599999999999998</totalGrade>
                <usualGrade>20.9</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>21</age>
            <enrollYear>2023</enrollYear>
            <gender>female</gender>
            <id>201250390</id>
            <major>Geography</major>
            <name>Edward</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>16.3</finalGrade>
                <totalGrade>41.0</totalGrade>
                <usualGrade>24.7</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>31.1</finalGrade>
                <totalGrade>39.2</totalGrade>
                <usualGrade>8.1</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>2.5</finalGrade>
                <totalGrade>20.8</totalGrade>
                <usualGrade>18.3</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>9.2</finalGrade>
                <totalGrade>24.7</totalGrade>
                <usualGrade>15.5</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>54.8</finalGrade>
                <totalGrade>64.39999999999999</totalGrade>
                <usualGrade>9.6</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>19</age>
            <enrollYear>2000</enrollYear>
            <gender>female</gender>
            <id>201250230</id>
            <major>Astronomy</major>
            <name>Samuel</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>17.3</finalGrade>
                <totalGrade>37.6</totalGrade>
                <usualGrade>20.3</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>28.6</finalGrade>
                <totalGrade>40.0</totalGrade>
                <usualGrade>11.4</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>13.4</finalGrade>
                <totalGrade>26.1</totalGrade>
                <usualGrade>12.7</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>13.4</finalGrade>
                <totalGrade>49.5</totalGrade>
                <usualGrade>36.1</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>51.0</finalGrade>
                <totalGrade>60.7</totalGrade>
                <usualGrade>9.7</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>22</age>
            <enrollYear>2001</enrollYear>
            <gender>female</gender>
            <id>201250964</id>
            <major>Software Engineering</major>
            <name>Katherine</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>22.3</finalGrade>
                <totalGrade>61.599999999999994</totalGrade>
                <usualGrade>39.3</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>25.2</finalGrade>
                <totalGrade>46.2</totalGrade>
                <usualGrade>21.0</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>2.6</finalGrade>
                <totalGrade>7.6</totalGrade>
                <usualGrade>5.0</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>51.0</finalGrade>
                <totalGrade>77.7</totalGrade>
                <usualGrade>26.7</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>9.2</finalGrade>
                <totalGrade>43.8</totalGrade>
                <usualGrade>34.6</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>23</age>
            <enrollYear>2012</enrollYear>
            <gender>female</gender>
            <id>201250545</id>
            <major>Astronomy</major>
            <name>Peter</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>11.3</finalGrade>
                <totalGrade>22.5</totalGrade>
                <usualGrade>11.2</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>32.9</finalGrade>
                <totalGrade>40.8</totalGrade>
                <usualGrade>7.9</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>35.8</finalGrade>
                <totalGrade>37.8</totalGrade>
                <usualGrade>2.0</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>18.5</finalGrade>
                <totalGrade>36.9</totalGrade>
                <usualGrade>18.4</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>45.1</finalGrade>
                <totalGrade>75.0</totalGrade>
                <usualGrade>29.9</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>18</age>
            <enrollYear>2011</enrollYear>
            <gender>male</gender>
            <id>201250631</id>
            <major>Software Engineering</major>
            <name>Fiona</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>40.4</finalGrade>
                <totalGrade>69.0</totalGrade>
                <usualGrade>28.6</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>2.1</finalGrade>
                <totalGrade>19.8</totalGrade>
                <usualGrade>17.7</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>49.3</finalGrade>
                <totalGrade>58.599999999999994</totalGrade>
                <usualGrade>9.3</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>3.3</finalGrade>
                <totalGrade>9.399999999999999</totalGrade>
                <usualGrade>6.1</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>10.4</finalGrade>
                <totalGrade>19.5</totalGrade>
                <usualGrade>9.1</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>19</age>
            <enrollYear>2010</enrollYear>
            <gender>female</gender>
            <id>201250107</id>
            <major>Finance</major>
            <name>Katherine</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>8.0</finalGrade>
                <totalGrade>34.9</totalGrade>
                <usualGrade>26.9</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>56.0</finalGrade>
                <totalGrade>67.5</totalGrade>
                <usualGrade>11.5</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>25.9</finalGrade>
                <totalGrade>45.8</totalGrade>
                <usualGrade>19.9</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>7.6</finalGrade>
                <totalGrade>16.1</totalGrade>
                <usualGrade>8.5</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>14.3</finalGrade>
                <totalGrade>36.2</totalGrade>
                <usualGrade>21.9</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>23</age>
            <enrollYear>2009</enrollYear>
            <gender>female</gender>
            <id>201250704</id>
            <major>Astronomy</major>
            <name>Ursula</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>51.9</finalGrade>
                <totalGrade>58.5</totalGrade>
                <usualGrade>6.6</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>34.0</finalGrade>
                <totalGrade>55.2</totalGrade>
                <usualGrade>21.2</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>55.7</finalGrade>
                <totalGrade>66.2</totalGrade>
                <usualGrade>10.5</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>55.4</finalGrade>
                <totalGrade>62.6</totalGrade>
                <usualGrade>7.2</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>22.6</finalGrade>
                <totalGrade>50.2</totalGrade>
                <usualGrade>27.6</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>20</age>
            <enrollYear>1996</enrollYear>
            <gender>male</gender>
            <id>201250974</id>
            <major>Astronomy</major>
            <name>Fiona</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>20.6</finalGrade>
                <totalGrade>57.0</totalGrade>
                <usualGrade>36.4</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>7.1</finalGrade>
                <totalGrade>33.3</totalGrade>
                <usualGrade>26.2</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>57.9</finalGrade>
                <totalGrade>96.3</totalGrade>
                <usualGrade>38.4</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>56.8</finalGrade>
                <totalGrade>78.69999999999999</totalGrade>
                <usualGrade>21.9</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>38.6</finalGrade>
                <totalGrade>46.1</totalGrade>
                <usualGrade>7.5</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>20</age>
            <enrollYear>1998</enrollYear>
            <gender>male</gender>
            <id>201250078</id>
            <major>Geography</major>
            <name>Franklin</name>
        </studentInfo>
    </ea:studentGrade>
    <ea:studentGrade>
        <courseGrades>
            <courseInfo>
                <courseName>Machine Learning</courseName>
            </courseInfo>
            <grade>
                <finalGrade>29.0</finalGrade>
                <totalGrade>63.8</totalGrade>
                <usualGrade>34.8</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Big Data Analysis</courseName>
            </courseInfo>
            <grade>
                <finalGrade>6.4</finalGrade>
                <totalGrade>44.9</totalGrade>
                <usualGrade>38.5</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Cloud Native</courseName>
            </courseInfo>
            <grade>
                <finalGrade>54.1</finalGrade>
                <totalGrade>55.9</totalGrade>
                <usualGrade>1.8</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing I</courseName>
            </courseInfo>
            <grade>
                <finalGrade>24.4</finalGrade>
                <totalGrade>25.7</totalGrade>
                <usualGrade>1.3</usualGrade>
            </grade>
        </courseGrades>
        <courseGrades>
            <courseInfo>
                <courseName>Software Engineering and Computing II</courseName>
            </courseInfo>
            <grade>
                <finalGrade>4.2</finalGrade>
                <totalGrade>41.0</totalGrade>
                <usualGrade>36.8</usualGrade>
            </grade>
        </courseGrades>
        <studentInfo>
            <age>35</age>
            <enrollYear>2020</enrollYear>
            <gender>man</gender>
            <id>201250001</id>
            <major>Computer Science</major>
            <name>Bond</name>
        </studentInfo>
    </ea:studentGrade>
</ea:studentGradeList>
```

## XML Processing(XSLT, SAX)

### 以课程为聚合的成绩单 Schema

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:ea="educational-admin"
           xmlns:am="archive-management"
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="educational-admin">

    <xs:import namespace="archive-management" schemaLocation="../archive-management/StudentInfo.xsd"/>
    <xs:include schemaLocation="CourseInfo.xsd"/>
    <xs:include schemaLocation="Grade.xsd"/>

    <xs:element name="courseGradeList">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="courseGrade" type="ea:courseGrade" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:complexType name="courseGrade">
        <xs:sequence>
            <xs:element name="courseInfo" type="ea:courseInfo"/>
            <xs:element name="courseGrades" type="ea:courseStudentGrade"/>
        </xs:sequence>
    </xs:complexType>

    <xs:complexType name="courseStudentGrade">
        <xs:sequence>
            <xs:element name="studentInfo" type="am:studentInfo"/>
            <xs:element name="grade" type="ea:grade" maxOccurs="unbounded"/>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

### XML 转化

使用如下 Java 脚本进行转化

```java
public class EducationalAdminServiceImpl implements EducationalAdminService {

    /**
     * 处理课程成绩单
     */
    @Override
    public void handleCourseGrade() {
        // 读取 StudentGrades.xml
        String documentPath = "src/main/resources/educational-admin/StudentGrades.xml";
        List<StudentGrade> studentGrades;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(documentPath);
            studentGrades = readStudentGradeFromDocument(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 转换为 CourseGrades
        List<CourseGrade> courseGrades = convertStudentGrades2CourseGrades(studentGrades);
        // 按照总评成绩进行排序
        courseGrades.forEach(courseGrade -> courseGrade.getStudentGrades().sort((o1, o2) -> o2.getGrade().getTotalGrade().compareTo(o1.getGrade().getTotalGrade())));

        DocumentUtils.toXML(new CourseGradeList(courseGrades), "src/main/resources/educational-admin/SortedCourseGrades.xml", CourseGradeList.class);

        // 保留任意不及格成绩
        courseGrades.forEach(courseGrade -> courseGrade.setStudentGrades(courseGrade.getStudentGrades().stream().filter(studentGrade -> studentGrade.getGrade().getTotalGrade() < 60).collect(Collectors.toList())));

        DocumentUtils.toXML(new CourseGradeList(courseGrades), "src/main/resources/educational-admin/FailedCourseGrades.xml", CourseGradeList.class);
    }

    private List<StudentGrade> readStudentGradeFromDocument(Document document) {
        List<StudentGrade> studentGrades = new ArrayList<>();
        NodeList nodelist = document.getElementsByTagName("ea:studentGradeList");
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                StudentGrade studentGrade = new StudentGrade();

                // handle studentCourseGrade
                List<StudentCourseGrade> courseGrades = new ArrayList<>();
                NodeList courseGradesNodeList = element.getElementsByTagName("courseGrades");
                for (int j = 0; j < courseGradesNodeList.getLength(); j++) {
                    Node courseGradeNode = courseGradesNodeList.item(j);
                    if (courseGradeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element courseGradeElement = (Element) courseGradeNode;
                        StudentCourseGrade courseGrade = new StudentCourseGrade();

                        Element courseInfo = (Element) courseGradeElement.getElementsByTagName("courseInfo").item(0);
                        courseGrade.setCourseInfo(new CourseInfo(courseInfo.getElementsByTagName("courseName").item(0).getTextContent()));
                        Element grade = (Element) courseGradeElement.getElementsByTagName("grade").item(0);
                        double usualGrade = Double.parseDouble(grade.getElementsByTagName("usualGrade").item(0).getTextContent());
                        double finalGrade = Double.parseDouble(grade.getElementsByTagName("finalGrade").item(0).getTextContent());
                        double totalGrade = Double.parseDouble(grade.getElementsByTagName("totalGrade").item(0).getTextContent());
                        courseGrade.setGrade(Grade.builder()
                                .usualGrade(usualGrade)
                                .finalGrade(finalGrade)
                                .totalGrade(totalGrade)
                                .build());

                        courseGrades.add(courseGrade);
                    }
                }

                // handle studentInfo
                StudentInfo studentInfo = null;
                Node studentInfoNode = element.getElementsByTagName("studentInfo").item(0);
                if (studentInfoNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentInfoElement = (Element) studentInfoNode;
                    studentInfo = new StudentInfo(studentInfoElement.getElementsByTagName("id").item(0).getTextContent(),
                            studentInfoElement.getElementsByTagName("name").item(0).getTextContent(),
                            Integer.parseInt(studentInfoElement.getElementsByTagName("age").item(0).getTextContent()),
                            studentInfoElement.getElementsByTagName("gender").item(0).getTextContent(),
                            studentInfoElement.getElementsByTagName("major").item(0).getTextContent(),
                            Integer.parseInt(studentInfoElement.getElementsByTagName("enrollYear").item(0).getTextContent())
                    );
                }


                studentGrade.setCourseGrades(courseGrades);
                studentGrade.setStudentInfo(studentInfo);
                studentGrades.add(studentGrade);
            }
        }

        return studentGrades;
    }

    private List<CourseGrade> convertStudentGrades2CourseGrades(List<StudentGrade> studentGrades) {
        List<CourseGrade> targetCourseGrades = new ArrayList<>();
        studentGrades.forEach(studentGrade -> {
            StudentInfo studentInfo = studentGrade.getStudentInfo();
            List<StudentCourseGrade> courseGrades = studentGrade.getCourseGrades();
            courseGrades.forEach(courseGrade -> {
                CourseStudentGrade courseStudentGrade = CourseStudentGrade.builder()
                        .studentInfo(studentInfo)
                        .grade(courseGrade.getGrade())
                        .build();
                if (targetCourseGrades.stream()
                        .noneMatch(targetCourseGrade -> targetCourseGrade.getCourseInfo().getCourseName().equals(courseGrade.getCourseInfo().getCourseName()))) {
                    // 说明此时在结果中还没有这个课程
                    targetCourseGrades.add(CourseGrade.builder()
                            .courseInfo(courseGrade.getCourseInfo())
                            .studentGrades(new ArrayList<>(Collections.singletonList(courseStudentGrade)))
                            .build());
                } else {
                    // 说明在结果中已经有了这个课程
                    targetCourseGrades.stream()
                            .filter(targetCourseGrade -> targetCourseGrade.getCourseInfo().getCourseName().equals(courseGrade.getCourseInfo().getCourseName()))
                            .forEach(targetCourseGrade -> targetCourseGrade.getStudentGrades().add(courseStudentGrade));
                }
            });
        });
        return targetCourseGrades;
    }
}
```

### XML 过滤

使用 XSLT 和 SAX 进行过滤（两种方式）

SAX Handler 定义如下：

```java
public class FilterXML {

    private static final String INPUT_FILE = "input.xml";
    private static final String OUTPUT_FILE = "output.xml";

    public static void main(String[] args) throws Exception {
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        ContentFilter contentHandler = new ContentFilter();
        saxParser.parse(inputFile, contentHandler);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        StreamResult result = new StreamResult(writer);

        Source source = new SAXSource(contentHandler.getXMLReader(), contentHandler.getInputSource());
        transformer.transform(source, result);

        System.out.println("Output to file saved!");
    }

    private static class ContentFilter extends DefaultHandler {
        private boolean inTotalGradeTag;
        private int totalGrade;
        private StringBuilder characters = new StringBuilder();
        private Deque<String> elementStack = new ArrayDeque<>();

        public XMLReader getXMLReader() {
            return null;
        }

        public InputSource getInputSource() {
            return new InputSource(new StringReader(this.characters.toString()));
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            this.elementStack.push(qName);
            if ("totalGrade".equals(qName)) {
                this.inTotalGradeTag = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            this.elementStack.pop();
            if ("totalGrade".equals(qName)) {
                this.inTotalGradeTag = false;
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String string = new String(ch, start, length);
            if (this.inTotalGradeTag) {
                try {
                    this.totalGrade = Integer.parseInt(string);
                    if (this.totalGrade < 60) {
                        // print the entire student record with grades less than 60
                        for (String s : this.elementStack) {
                            startElement("", s, s, null);
                        }
                        characters(ch, start, length);
                        while (!"student".equals(this.elementStack.peek())) {
                            endElement("", this.elementStack.pop(), this.elementStack.pop());
                        }
                        endElement("", "student", "student");
                    }
                } catch (NumberFormatException e) {
                    throw new SAXException(e.getMessage(), e);
                }
            }
            this.characters.append(string);
        }
    }
}
```

### 结果展示

排序后的结果，`SortedCourseGrades.xml`：

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ns2:courseGradeList xmlns:ns2="educational-admin">
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Machine Learning</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>40.4</finalGrade>
                <totalGrade>69.0</totalGrade>
                <usualGrade>28.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>29.0</finalGrade>
                <totalGrade>63.8</totalGrade>
                <usualGrade>34.8</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>22.3</finalGrade>
                <totalGrade>61.599999999999994</totalGrade>
                <usualGrade>39.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>51.9</finalGrade>
                <totalGrade>58.5</totalGrade>
                <usualGrade>6.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>20.6</finalGrade>
                <totalGrade>57.0</totalGrade>
                <usualGrade>36.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>16.3</finalGrade>
                <totalGrade>41.0</totalGrade>
                <usualGrade>24.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>17.3</finalGrade>
                <totalGrade>37.6</totalGrade>
                <usualGrade>20.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>8.0</finalGrade>
                <totalGrade>34.9</totalGrade>
                <usualGrade>26.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>11.3</finalGrade>
                <totalGrade>22.5</totalGrade>
                <usualGrade>11.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>12.9</finalGrade>
                <totalGrade>13.6</totalGrade>
                <usualGrade>0.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Big Data Analysis</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>56.0</finalGrade>
                <totalGrade>67.5</totalGrade>
                <usualGrade>11.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>34.0</finalGrade>
                <totalGrade>55.2</totalGrade>
                <usualGrade>21.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>54.0</finalGrade>
                <totalGrade>54.1</totalGrade>
                <usualGrade>0.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>25.2</finalGrade>
                <totalGrade>46.2</totalGrade>
                <usualGrade>21.0</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>6.4</finalGrade>
                <totalGrade>44.9</totalGrade>
                <usualGrade>38.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>32.9</finalGrade>
                <totalGrade>40.8</totalGrade>
                <usualGrade>7.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>28.6</finalGrade>
                <totalGrade>40.0</totalGrade>
                <usualGrade>11.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>31.1</finalGrade>
                <totalGrade>39.2</totalGrade>
                <usualGrade>8.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>7.1</finalGrade>
                <totalGrade>33.3</totalGrade>
                <usualGrade>26.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>2.1</finalGrade>
                <totalGrade>19.8</totalGrade>
                <usualGrade>17.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Cloud Native</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>57.9</finalGrade>
                <totalGrade>96.3</totalGrade>
                <usualGrade>38.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>52.8</finalGrade>
                <totalGrade>82.4</totalGrade>
                <usualGrade>29.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>55.7</finalGrade>
                <totalGrade>66.2</totalGrade>
                <usualGrade>10.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>49.3</finalGrade>
                <totalGrade>58.599999999999994</totalGrade>
                <usualGrade>9.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>54.1</finalGrade>
                <totalGrade>55.9</totalGrade>
                <usualGrade>1.8</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>25.9</finalGrade>
                <totalGrade>45.8</totalGrade>
                <usualGrade>19.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>35.8</finalGrade>
                <totalGrade>37.8</totalGrade>
                <usualGrade>2.0</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>13.4</finalGrade>
                <totalGrade>26.1</totalGrade>
                <usualGrade>12.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>2.5</finalGrade>
                <totalGrade>20.8</totalGrade>
                <usualGrade>18.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>2.6</finalGrade>
                <totalGrade>7.6</totalGrade>
                <usualGrade>5.0</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Software Engineering and Computing I</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>56.8</finalGrade>
                <totalGrade>78.69999999999999</totalGrade>
                <usualGrade>21.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>51.0</finalGrade>
                <totalGrade>77.7</totalGrade>
                <usualGrade>26.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>45.4</finalGrade>
                <totalGrade>76.8</totalGrade>
                <usualGrade>31.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>55.4</finalGrade>
                <totalGrade>62.6</totalGrade>
                <usualGrade>7.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>13.4</finalGrade>
                <totalGrade>49.5</totalGrade>
                <usualGrade>36.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>18.5</finalGrade>
                <totalGrade>36.9</totalGrade>
                <usualGrade>18.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>24.4</finalGrade>
                <totalGrade>25.7</totalGrade>
                <usualGrade>1.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>9.2</finalGrade>
                <totalGrade>24.7</totalGrade>
                <usualGrade>15.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>7.6</finalGrade>
                <totalGrade>16.1</totalGrade>
                <usualGrade>8.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>3.3</finalGrade>
                <totalGrade>9.399999999999999</totalGrade>
                <usualGrade>6.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Software Engineering and Computing II</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>45.1</finalGrade>
                <totalGrade>75.0</totalGrade>
                <usualGrade>29.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>54.8</finalGrade>
                <totalGrade>64.39999999999999</totalGrade>
                <usualGrade>9.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>51.0</finalGrade>
                <totalGrade>60.7</totalGrade>
                <usualGrade>9.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>22.6</finalGrade>
                <totalGrade>50.2</totalGrade>
                <usualGrade>27.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>38.6</finalGrade>
                <totalGrade>46.1</totalGrade>
                <usualGrade>7.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>9.2</finalGrade>
                <totalGrade>43.8</totalGrade>
                <usualGrade>34.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>4.2</finalGrade>
                <totalGrade>41.0</totalGrade>
                <usualGrade>36.8</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>14.3</finalGrade>
                <totalGrade>36.2</totalGrade>
                <usualGrade>21.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>9.7</finalGrade>
                <totalGrade>30.599999999999998</totalGrade>
                <usualGrade>20.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>10.4</finalGrade>
                <totalGrade>19.5</totalGrade>
                <usualGrade>9.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
</ns2:courseGradeList>
```

过滤后的结果，`FailedCourseGrades.xml`：

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ns2:courseGradeList xmlns:ns2="educational-admin">
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Machine Learning</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>51.9</finalGrade>
                <totalGrade>58.5</totalGrade>
                <usualGrade>6.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>20.6</finalGrade>
                <totalGrade>57.0</totalGrade>
                <usualGrade>36.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>16.3</finalGrade>
                <totalGrade>41.0</totalGrade>
                <usualGrade>24.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>17.3</finalGrade>
                <totalGrade>37.6</totalGrade>
                <usualGrade>20.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>8.0</finalGrade>
                <totalGrade>34.9</totalGrade>
                <usualGrade>26.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>11.3</finalGrade>
                <totalGrade>22.5</totalGrade>
                <usualGrade>11.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>12.9</finalGrade>
                <totalGrade>13.6</totalGrade>
                <usualGrade>0.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Big Data Analysis</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>34.0</finalGrade>
                <totalGrade>55.2</totalGrade>
                <usualGrade>21.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>54.0</finalGrade>
                <totalGrade>54.1</totalGrade>
                <usualGrade>0.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>25.2</finalGrade>
                <totalGrade>46.2</totalGrade>
                <usualGrade>21.0</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>6.4</finalGrade>
                <totalGrade>44.9</totalGrade>
                <usualGrade>38.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>32.9</finalGrade>
                <totalGrade>40.8</totalGrade>
                <usualGrade>7.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>28.6</finalGrade>
                <totalGrade>40.0</totalGrade>
                <usualGrade>11.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>31.1</finalGrade>
                <totalGrade>39.2</totalGrade>
                <usualGrade>8.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>7.1</finalGrade>
                <totalGrade>33.3</totalGrade>
                <usualGrade>26.2</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>2.1</finalGrade>
                <totalGrade>19.8</totalGrade>
                <usualGrade>17.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Cloud Native</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>49.3</finalGrade>
                <totalGrade>58.599999999999994</totalGrade>
                <usualGrade>9.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>54.1</finalGrade>
                <totalGrade>55.9</totalGrade>
                <usualGrade>1.8</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>25.9</finalGrade>
                <totalGrade>45.8</totalGrade>
                <usualGrade>19.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>35.8</finalGrade>
                <totalGrade>37.8</totalGrade>
                <usualGrade>2.0</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>13.4</finalGrade>
                <totalGrade>26.1</totalGrade>
                <usualGrade>12.7</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>2.5</finalGrade>
                <totalGrade>20.8</totalGrade>
                <usualGrade>18.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>2.6</finalGrade>
                <totalGrade>7.6</totalGrade>
                <usualGrade>5.0</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Software Engineering and Computing I</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>13.4</finalGrade>
                <totalGrade>49.5</totalGrade>
                <usualGrade>36.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>18.5</finalGrade>
                <totalGrade>36.9</totalGrade>
                <usualGrade>18.4</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>24.4</finalGrade>
                <totalGrade>25.7</totalGrade>
                <usualGrade>1.3</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>9.2</finalGrade>
                <totalGrade>24.7</totalGrade>
                <usualGrade>15.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>7.6</finalGrade>
                <totalGrade>16.1</totalGrade>
                <usualGrade>8.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>3.3</finalGrade>
                <totalGrade>9.399999999999999</totalGrade>
                <usualGrade>6.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
    <ns2:courseGrade>
        <courseInfo>
            <courseName>Software Engineering and Computing II</courseName>
        </courseInfo>
        <studentGrades>
            <grade>
                <finalGrade>22.6</finalGrade>
                <totalGrade>50.2</totalGrade>
                <usualGrade>27.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>38.6</finalGrade>
                <totalGrade>46.1</totalGrade>
                <usualGrade>7.5</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>9.2</finalGrade>
                <totalGrade>43.8</totalGrade>
                <usualGrade>34.6</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>4.2</finalGrade>
                <totalGrade>41.0</totalGrade>
                <usualGrade>36.8</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>14.3</finalGrade>
                <totalGrade>36.2</totalGrade>
                <usualGrade>21.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>9.7</finalGrade>
                <totalGrade>30.599999999999998</totalGrade>
                <usualGrade>20.9</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
        <studentGrades>
            <grade>
                <finalGrade>10.4</finalGrade>
                <totalGrade>19.5</totalGrade>
                <usualGrade>9.1</usualGrade>
            </grade>
            <studentInfo>
                <age>21</age>
                <enrollYear>2023</enrollYear>
                <gender>female</gender>
                <id>201250390</id>
                <major>Geography</major>
                <name>Edward</name>
            </studentInfo>
        </studentGrades>
    </ns2:courseGrade>
</ns2:courseGradeList>
```
