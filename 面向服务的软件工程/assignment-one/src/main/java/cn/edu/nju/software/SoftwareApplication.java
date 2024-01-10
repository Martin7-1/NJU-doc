package cn.edu.nju.software;

import cn.edu.nju.software.service.ArchiveManagementService;
import cn.edu.nju.software.service.EducationalAdminService;
import cn.edu.nju.software.service.impl.ArchiveManagementServiceImpl;
import cn.edu.nju.software.service.impl.EducationalAdminServiceImpl;

public class SoftwareApplication {

    private final ArchiveManagementService archiveManagementService;
    private final EducationalAdminService educationalAdminService;

    public SoftwareApplication() {
        archiveManagementService = new ArchiveManagementServiceImpl();
        educationalAdminService = new EducationalAdminServiceImpl(archiveManagementService);
    }

    public static void main(String[] args) {
        SoftwareApplication application = new SoftwareApplication();
        // XML Processing
        // application.educationalAdminService.handleStudentGrade();
        // SAX Processing
        application.educationalAdminService.handleCourseGrade();
    }
}
