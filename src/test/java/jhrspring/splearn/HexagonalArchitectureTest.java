package jhrspring.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "jhrspring.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {
    @ArchTest
    void hexagonalArchitectureTest(JavaClasses classes){
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("jhrspring.splearn.domain..")
                .layer("application").definedBy("jhrspring.splearn.application..")
                .layer("adapter").definedBy("jhrspring.splearn.adapter..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application","adapter")
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }

}
