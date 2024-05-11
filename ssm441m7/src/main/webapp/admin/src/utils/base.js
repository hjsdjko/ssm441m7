const base = {
    get() {
        return {
            url : "http://localhost:8080/ssm441m7/",
            name: "ssm441m7",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/ssm441m7/front/h5/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "同城家政服务小程序"
        } 
    }
}
export default base
