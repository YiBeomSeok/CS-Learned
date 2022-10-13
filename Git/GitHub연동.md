1. 생성
- GitHub에서 Repository 생성  

2. 연동하기를 원하는 로컬 디렉토리로 이동해서 우클릭 후 Git Bash Here 클릭 (혹은 git bash에서 cd 명령어를 통해 이동)  

```
git init
git remote add origin https://github.com/YiBeomSeok/TIL.git
git branch -M main

git commit -m "first commit"


git push -u origin main
```
`git init` 명령어는 로컬에서 Git 저장소를 초기화한다.
`git remote add origin https://github.com/YiBeomSeok/TIL.git`는 원하는 깃허브 원격 레포지토리를 git에 origin이라는 이름으로 추가해준다. 여기서 origin은 
주소 대신에 입력할 별칭정도로 알면 된다.
`git branch -M main`은 현재 branch 위치를 main branch로 이동시킨다. 예를 들어 bmsk_kin 브랜치로 이동하고자 하면  
`git branch -M bmsk_kun`으로 입력 해주면 된다.  

`git commit -m "메시지 내용"`은 커밋 메시지를 지정한다.


`git push -u origin main` 위에서 추가한 origin을 main에 push할 수 있다.

