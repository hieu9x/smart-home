package com.example.myapplication.Model;

public class UserAndModule {
    private String userId, userName,module1,module2,module3,module4;

    public UserAndModule(){
    }
    public UserAndModule(String userId, String userName,String module1,String module2,String module3,String module4){
        this.userId=userId;
        this.userName=userName;
        this.module3=module3;
        this.module2=module2;
        this.module1=module1;
        this.module4=module4;
    }
    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }
    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }
    public String getModule1() {
        return module1;
    }

    public void setModule1(String module1) {
        this.module1 = module1;
    }
    public String getModule2() {
        return module2;
    }

    public void setModule2(String module2) {
        this.module2 = module2;
    }
    public String getModule3() {
        return module3;
    }

    public void setModule3(String module3) {
        this.module3 = module3;
    }
    public String getModule4() {
        return module4;
    }

    public void setModule4(String module4) {
        this.module4 = module4;
    }

}

