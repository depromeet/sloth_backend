# 도커 종료
sudo docker stop "sloth-real1"
sudo docker rm -f "sloth-real1"

#미사용 도커 이미지 삭제
sudo docker image prune -f


# 도커 이미지 pull
sudo docker pull dbfgml741/sloth

sleep 5

# 도커 실행
sudo docker run -d --name sloth-real1 -v /home/ec2-user/nanagong:/nanagong -e --rm -p 80:8081 -e "SPRING_PROFILES_ACTIVE=real1" -e "nanagong.enc.password=nanagong-backend!@#" dbfgml741/sloth:latest