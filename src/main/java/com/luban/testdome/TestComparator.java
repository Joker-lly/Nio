package com.luban.testdome;

import java.util.*;

public class TestComparator {
    public static void main(String[] args) {
        test4();
    }


    public static void test1(){
        List<Student> stus = new ArrayList<Student>(){
            {
                add(new Student("张三", 30));
                add(new Student("李四", 20));
                add(new Student("王五", 60));
            }
        };
        for (Student student : stus) {
            System.out.println(student);
        }
        System.out.println("==================");
        //对users按年龄进行排序
        Collections.sort(stus, new Comparator<Student>() {

            @Override
            public int compare(Student s1, Student s2) {
                // 升序
                //return s1.getAge()-s2.getAge();
                return s1.getAge().compareTo(s2.getAge());
                // 降序
                // return s2.getAge()-s1.getAge();
                // return s2.getAge().compareTo(s1.getAge());
            }
        });
        for (Student student : stus) {
            System.out.println(student);
        }

    }


    public static void test2(){
        List<Student> stus = new ArrayList<Student>(){
            {
                add(new Student("张三", 30));
                add(new Student("李四", 20));
                add(new Student("王五", 60));
            }
        };
        for (Student student : stus) {
            System.out.println(student);
        }
        System.out.println("==================");
        //对users按年龄进行排序
        Collections.sort(stus, (s1,s2)->(s1.getAge()-s2.getAge()));
        for (Student student : stus) {
            System.out.println(student);
        }
    }


    public static void test3(){
        List<Student> stus = new ArrayList<Student>(){
            {
                add(new Student("张三", 30, 1));
                add(new Student("李四", 20, 2));
                add(new Student("王五", 40, 3));
                add(new Student("赵六", 30, 4));
                add(new Student("陈七", 40, 5));
                add(new Student("周八", 20, 6));
            }
        };
        for (Student student : stus) {
            System.out.println(student);
        }
        System.out.println("==================");
        Collections.sort(stus,new Comparator<Student>() {

            @Override
            public int compare(Student s1, Student s2) {
                int flag;
                // 首选按年龄升序排序
                flag = s1.getAge()-s2.getAge();
                if(flag==0){
                    // 再按学号升序排序
                    flag = s1.getNum()-s2.getNum();
                }
                return flag;
            }
        });

        for (Student student : stus) {
            System.out.println(student);
        }
    }


    public static void test4(){
        String[] order = {"语文","数学","英语","物理","化学","生物","政治","历史","地理","总分"};
        final List<String> definedOrder = Arrays.asList(order);
        List<String> list = new ArrayList<String>(){
            {
                add("总分");
                add("英语");
                add("政治");
                add("总分");
                add("数学");
            }
        };
        Collections.sort(list,new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                int io1 = definedOrder .indexOf(o1);
                int io2 = definedOrder .indexOf(o2);
                return io1-io2;
            }
        });

        for(String s:list){
            System.out.print(s+"   ");
        }
    }

    static class Student{
        private String name;
        private Integer age;
        private Integer num;

        public Integer getNum() {
            return num;
        }

        public Student(String name, Integer age, Integer num) {
            this.name = name;
            this.age = age;
            this.num = num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Student(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", num=" + num +
                    '}';
        }
    }
}
