package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;


@Component
public class FileIOUtils {

	private static final String ROOT_PATH = "C://fileTest";
	private static final Integer BUFFER_SIZE = 8000;	//TCP Socket의 기본 버퍼 크기: 8KB

	private final StopWatch mpWatch = new StopWatch("mpWatch");
	private final StopWatch streamWatch = new StopWatch("streamWatch");


	public void saveFile(String fileUuid, HttpServletRequest request) throws IOException{
		streamWatch.start();

		Path fileDirectoryPath = Paths.get(ROOT_PATH).toAbsolutePath().normalize();
		Path filePath = fileDirectoryPath.resolve(fileUuid).normalize();
		if(Files.notExists(fileDirectoryPath))
			Files.createDirectories(fileDirectoryPath);

		FileOutputStream outStream = new FileOutputStream(filePath.toFile(), true);
		ServletInputStream inStream = request.getInputStream();   //request로 넘어온 body를 읽기 위해 socket에 대한 open을 한다고 생각
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while((bytesRead = inStream.read(buffer)) != -1){	//inStream.read=> 클라가 천천히 보내서 서버가 더 빨리 읽을 경우 서버는 기다린다.(time out 설정 값으로 지정한 만큼만 기다린다.)
			outStream.write(buffer, 0, bytesRead);
		}
		outStream.close();

		streamWatch.stop();
		System.out.println(streamWatch.prettyPrint());
	}


	public void saveMultipartFile(String fileUuid, MultipartFile mpFiles) throws IOException {
		mpWatch.start();
		Path fileDirectoryPath = Paths.get(ROOT_PATH).toAbsolutePath().normalize();
		Path filePath = fileDirectoryPath.resolve(fileUuid).normalize();
		if(Files.notExists(fileDirectoryPath))
			Files.createDirectories(fileDirectoryPath);

		//file uuid가 고유하기 때문에 사실상 덮어쓸 일이 없음(파일은 수정의 개념이 없고 추가 삭제에 대한 개념만 있음)
		Files.copy(mpFiles.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		mpWatch.stop();
		System.out.println(mpWatch.prettyPrint());
	}
	
	
}
