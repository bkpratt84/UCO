package csDept;

import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class CourseFlowChartBean {

    public CourseFlowChartBean() {
        FlowChartCourse c = null;
        int cnt = 1;
        int lvl = 1;

        // Level 1
        c = new FlowChartCourse("HS", 0, lvl, cnt++,"HS AP");
        c.addPre("None", 1);
        addCourse(c);

        c = new FlowChartCourse("HS", 1, lvl, cnt++, "2 years HS algebra");
        c.addPre("None", 1);
        addCourse(c);

        c = new FlowChartCourse("HS", 2, lvl, cnt++, "HS Trigonometry");
        c.addPre("None", 1);
        addCourse(c);

        c = new FlowChartCourse("MATH", 1593, lvl, cnt++, "Plane Trigonometry");
        c.addPre("None", 1);
        addCourse(c);

        c = new FlowChartCourse("MATH", 1513, lvl, cnt++, "Coll Alg");
        c.addPre("None", 1);
        addCourse(c);

        c = new FlowChartCourse("MATH", 1555, lvl, cnt++, "Coll Alg & Trig");
        c.addPre("None", 1);
        addCourse(c);
        // End Level 1

        lvl++;
        cnt = 1;

        // Level 2
        c = new FlowChartCourse("CMSC", 1053, lvl, cnt++, "Com Tech");
        c.addPre("None", 1);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 1103 , lvl, cnt++, "Intro");
        c.addPre("HS", 1);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 1513, lvl, cnt++, "Beg Prog");
        c.addPre("CMSC", 1521);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 1521, lvl, cnt++, "BP Lab");
        c.addPre("CMSC", 1513);
        addCourse(c);

        c = new FlowChartCourse("MATH", 2313, lvl, cnt++, "Calc I");
        c.addPre("HS", 1);
        c.addPre("HS", 2);
        c.addPre("MATH", 1593);
        addCourse(c);

        c = new FlowChartCourse("STAT", 2103, lvl, cnt++, "Stat Method 2113 or 4113 or");
        c.addPre("MATH", 1513);
        addCourse(c);
        // End Level 2

        lvl++;
        cnt = 1;

        // Level 3
        c = new FlowChartCourse("CMSC", 2413, lvl, cnt++, "Vis Prog");
        c.addPre("CMSC", 1513);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 1613, lvl, cnt++, "Prog I");
        c.addPre("HS", 0);
        c.addPre("CMSC", 1513);
        c.addPre("MATH", 1513);
        c.addPre("MATH", 1555);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 1621, lvl, cnt++, "P1 Lab");
        c.addPre("CMSC", 1613);
        c.addPre("MATH", 2313);
        addCourse(c);

        c = new FlowChartCourse("MATH", 2323, lvl, cnt++, "Calc II");
        c.addPre("MATH", 2313);
        addCourse(c);

        c = new FlowChartCourse("MATH", 2333, lvl, cnt++, "Calc III");
        c.addPre("MATH", 2323);
        addCourse(c);

        c = new FlowChartCourse("MATH", 3143, lvl, cnt++, "Linear Alg");
        c.addPre("CMSC", 2833);
        addCourse(c);
        // End Level 3

        lvl++;
        cnt = 1;

        // Level 4
        c = new FlowChartCourse("CMSC", 2833, lvl, cnt++, "Comp Org I");
        c.addPre("CMSC", 1613);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 2123, lvl, cnt++, "Disc Struct");
        c.addPre("CMSC", 1613);
        c.addPre("MATH", 2313);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 2613, lvl, cnt++, "Prog II");
        c.addPre("CMSC", 1613);
        c.addPre("CMSC", 2621);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 2621, lvl, cnt++, "PII Lab");
        c.addPre("CMSC", 2613);
        addCourse(c);
        // End Level 4

        lvl++;
        cnt = 1;

        // Level 5

        c = new FlowChartCourse("CMSC", 3413, lvl, cnt++, "Adv VP");
        c.addPre("CMSC", 2613);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 3103, lvl, cnt++, "OO SW");
        c.addPre("CMSC", 2613);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 3833, lvl, cnt++, "Comp Org II");
        c.addPre("CMSC", 2833);
        c.addAvail("Fall", 2015);
        c.addAvail("Fall", 2016);
        addCourse(c);
        // End Level 5

        lvl++;
        cnt = 1;

        // Level 6
        c = new FlowChartCourse("CMSC", 4003, lvl, cnt++, "DataBase");
        c.addPre("CMSC", 2613);
        c.addPre("MATH", 2323);
        c.addPre("STAT", 2103);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4283, lvl, cnt++, "SW Engr I");
        c.addPre("CMSC", 3103);
        c.addPre("CMSC", 2613);
        c.addPre("MATH", 2323);
        c.addPre("STAT", 2103);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 3613, lvl, cnt++, "DS & ALG");
        c.addPre("CMSC", 2123);
        c.addPre("CMSC", 2613);
        c.addPre("MATH", 2323);
        c.addPre("STAT", 2103);
        addCourse(c);
        // End Level 6

        lvl++;
        cnt = 1;

        // Level 7
        c = new FlowChartCourse("CMSC", 4303, lvl, cnt++, "Mobile Apps");
        c.addPre("CMSC", 3103);
        c.addAvail("Fall", 2015);
        c.addAvail("Fall", 2016);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4373, lvl, cnt++, "Web Server");
        c.addPre("CMSC", 3103);
        c.addPre("CMSC", 4003);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4910, lvl, cnt++, "Seminar Courses");
        c.addPre("CMSC", 3613);
        c.addAvail("Fall", 2015);
        c.addAvail("Fall", 2016);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4930, lvl, cnt++, "Ind Study");
        c.addPre("CMSC", 3613);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4950, lvl, cnt++, "Internship");
        c.addPre("CMSC", 3613);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4153, lvl, cnt++, "OS");
        c.addPre("CMSC", 3613);
        addCourse(c);
        // End Level 7

        lvl++;
        cnt = 1;

        // Level 8
        c = new FlowChartCourse("CMSC", 4383, lvl, cnt++, "File Struct");
        c.addPre("CMSC", 3613);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4063, lvl, cnt++, "Network");
        c.addPre("CMSC", 3613);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4401, lvl, cnt++, "Ethics");
        c.addPre("CMSC", 3613);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4133, lvl, cnt++, "AI");
        c.addPre("CMSC", 3613);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4023, lvl, cnt++, "Prog Lang");
        c.addPre("CMSC", 3613);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4173, lvl, cnt++, "Translator");
        c.addPre("CMSC", 3613);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4273, lvl, cnt++, "Theory of Computing");
        c.addPre("CMSC", 3613);
        c.addAvail("Fall", 2015);
        c.addAvail("Fall", 2016);
        addCourse(c);

        c = new FlowChartCourse("CMSC", 4323, lvl, cnt++, "Security");
        c.addPre("CMSC", 3613);
        c.addAvail("Fall", 2015);
        c.addAvail("Fall", 2016);
        addCourse(c);
        // End Level 8

        lvl++;
        cnt = 1;

        // Level 9
        c = new FlowChartCourse("CMSC", 4513, lvl, cnt++, "SDD");
        c.addPre("CMSC", 4003);
        c.addPre("CMSC", 4283);
        addCourse(c);

        c = new FlowChartCourse("SE", 4423, lvl, cnt++, "SW Engr II");
        c.addPre("CMSC", 4283);
        c.addAvail("Fall", 2015);
        c.addAvail("Fall", 2016);
        addCourse(c);

        c = new FlowChartCourse("SE", 4433, lvl, cnt++, "SW Archi");
        c.addPre("CMSC", 4283);
        c.addAvail("Spring", 2015);
        c.addAvail("Spring", 2016);
        c.addAvail("Spring", 2017);
        addCourse(c);

        c = new FlowChartCourse("SE", 4513, lvl, cnt++, "Senior Proj");
        c.addPre("CMSC", 4003);
        c.addPre("SE", 4423);
        c.addPre("SE", 4433);
        addCourse(c);

        // End Level 9
    }

    public ArrayList<FlowChartCourse> getCourses() {
        return courses;
    }

    private void addCourse(FlowChartCourse c) {
        courses.add(c);
    }

    private final ArrayList<FlowChartCourse> courses = new ArrayList();
    
}
