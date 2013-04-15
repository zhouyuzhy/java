package org.csp.client;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.csp.client.exception.ServiceException;
import org.csp.client.service.IEncryptAndStoreService;
import org.csp.exception.KeyPairException;
import org.csp.keypair.IKeyPairFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Console {

	private IEncryptAndStoreService storeService;
	private IKeyPairFactory publicKeyPairFactory;
	private IKeyPairFactory privateKeyPairFactory;
	private String defaultPublicKeyFile;
	private String defaultPrivateKeyFile;

	public void setStoreService(IEncryptAndStoreService storeService) {
		this.storeService = storeService;
	}

	public void setDefaultPublicKeyFile(String defaultPublicKeyFile) {
		this.defaultPublicKeyFile = defaultPublicKeyFile;
	}

	public void setDefaultPrivateKeyFile(String defaultPrivateKeyFile) {
		this.defaultPrivateKeyFile = defaultPrivateKeyFile;
	}
	
	public void setPrivateKeyPairFactory(IKeyPairFactory privateKeyPairFactory) {
		this.privateKeyPairFactory = privateKeyPairFactory;
	}
	
	public void setPublicKeyPairFactory(IKeyPairFactory publicKeyPairFactory) {
		this.publicKeyPairFactory = publicKeyPairFactory;
	}
	
	

	public static void help() {
		System.err
				.println("Store help: java Console [-P {publicKeyFile}] -s {key1} {key2} ...");
		System.err
				.println("\t \t  java Console [-P privateKeyFile] -g {key1} {key2} ...");
		System.err.println("\t \t  java Console -e {key1} {key2} ...");
		System.err.println("\t \t  java Console -l");
	}

	public void save(String[] args, int startIndex) throws KeyPairException, ServiceException {
		if (args.length <= startIndex) {
			System.err.println("args����С��startIndex��");
			help();
			return;
		}
		
		if (!"-P".equals(args[startIndex]) && !"-s".equals(args[startIndex]) || args.length == startIndex + 1) {
			// args ������-P����-s��ͷ ���� �� -P or -s��β
			System.err.println("args ������-P����-s��ͷ ���� �� -P or -s��β��");
			help();
			return;
		}
		
		// ���ɹ�Կ����
		PublicKey publicKey = null;
		if ("-P".equals(args[startIndex])) {
			String publicKeyFile = args[++startIndex];
			publicKey = publicKeyPairFactory.generatePublicKey(publicKeyFile);
		} else {
			publicKey = publicKeyPairFactory.generatePublicKey(defaultPublicKeyFile);
		}
		
		//��ȡ���м�����Ϊ�洢key
		int tempStartIndex = startIndex;
		for (int i = 0; i < args.length; i++) {
			if ("-s".equals(args[i]))
				startIndex = i + 1; // ��ʼ��ȡ���м�
		}
		if(tempStartIndex == startIndex){
			// ������-s
			System.err.println("û��ָ��-s�Լ��洢key");
			help();
			return;
		}
			
		String[] key = new String[args.length - startIndex];
		for (int i = 0; i < key.length; i++) {
			key[i] = args[startIndex + i];
		}
		//���û������ն˻�ȡ�������Ҫ���ܴ洢������
		String value = readPassword("Input password to encrypt and save: ");

		storeService.encryptAndSave(key, value, publicKey);
		System.out.println("���ܴ洢��ϣ�");
	}

	public void get(String[] args, int startIndex) throws KeyPairException, ServiceException {
		if (args.length <= startIndex) {
			System.err.println("args����С��startIndex��");
			help();
			return;
		}
		
		if (!"-P".equals(args[startIndex]) && !"-g".equals(args[startIndex]) || args.length == startIndex + 1) {
			// args ������-P����-s��ͷ ���� �� -P or -s��β
			System.err.println("args ������-P����-g��ͷ ���� �� -P or -g��β��");
			help();
			return;
		}
		
		String privateKeyFile = "";
		if ("-P".equals(args[startIndex])) {
			privateKeyFile = args[++startIndex];
		} else {
			privateKeyFile = defaultPrivateKeyFile;
		}
		
		String keyPassword = readPassword("������˽Կ�ļ�("+privateKeyFile+")�����룺");
		privateKeyPairFactory.setPassword(keyPassword);
		PrivateKey privateKey = privateKeyPairFactory.generatePrivateKey(privateKeyFile);
		
		int tempStartIndex = startIndex;
		for (int i = 0; i < args.length; i++) {
			if ("-g".equals(args[i]))
				startIndex = i + 1; // ��ʼ��ȡ���м�
		}
		if(tempStartIndex == startIndex){
			// ������-g
			System.err.println("û��ָ��-g�Լ��洢key");
			help();
			return;
		}
		String[] key = new String[args.length - startIndex];
		for (int i = 0; i < key.length; i++) {
			key[i] = args[startIndex + i];
		}
		
		System.out.println(storeService.getAndDecrypt(key, privateKey));
	}

	public void exists(String[] args, int startIndex) throws ServiceException {
		if (args.length <= startIndex) {
			System.err.println("args����С��startIndex��");
			help();
			return;
		}
		
		if (!"-e".equals(args[startIndex]) || args.length == startIndex + 1) {
			// args ������-e��ͷ ���� ��-e��β
			System.err.println("args ������-e��ͷ ���� ��-e��β��");
			help();
			return;
		}
		
		int tempStartIndex = startIndex;
		for (int i = 0; i < args.length; i++) {
			if ("-e".equals(args[i]))
				startIndex = i + 1; // ��ʼ��ȡ���м�
		}
		if(tempStartIndex == startIndex){
			// ������-e
			System.err.println("û��ָ��-e�Լ��洢key");
			help();
			return;
		}
		String[] key = new String[args.length - startIndex];
		for (int i = 0; i < key.length; i++) {
			key[i] = args[startIndex + i];
		}
		
		System.out.println(storeService.exists(key));
	}

	public void list(String[] args, int startIndex) throws ServiceException {
		if (!"-l".equals(args[startIndex]) || args.length != startIndex + 1) {
			// args ������-l��ͷ ���� ������-l��β
			System.err.println("args ������-l��ͷ ���� ������-l��β��");
			help();
			return;
		}
		String[][] keys = storeService.listKeys();
		for(String[] key : keys){
			System.out.println(Arrays.asList(key));
		}
	}

	public static String readPassword(String prompt) {
		if (System.console() != null)
			return String
					.valueOf(System.console().readPassword("[%s]", prompt));
		Scanner scanner = new Scanner(System.in);
		System.out.print(prompt);
		return scanner.next();
	}

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws KeyPairException 
	 */
	public static void main(String[] args) throws KeyPairException, ServiceException {
		ClassPathXmlApplicationContext applicationConext = new ClassPathXmlApplicationContext("beans.xml");
		Console console = applicationConext.getBean("console", Console.class);
		List<String> argList = Arrays.asList(args);
		if(argList==null)
			System.err.println("ArgList is NULL!");
		if(argList.contains("-s"))
			console.save(args, 0);
		else if (argList.contains("-g"))
			console.get(args, 0);
		else if(argList.contains("-e"))
			console.exists(args, 0);
		else if(argList.contains("-l"))
			console.list(args, 0);
	}

}
