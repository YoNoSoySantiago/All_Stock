package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import CustomExceptions.AlreadyProductExistException;
import CustomExceptions.UserExistException;
import CustomExceptions.ValueIsEmptyException;

public class AllStock{
	///////////////////////////////////////////////////////////////////////////////////////
	/*
	 * AUNQUE EL RETORNO ESTE EN VOID LOGICAMENTE ESTO SE CAMBIA DEPENDIENDO COMO LO
	 * NECESITEMOS ASU QUE CAMBIENLO A GUSTO SI ES NECESARIO
	 */

	// RELACIONES
	private Company companies;
	private User users;
	private Company actualCompany;

	
	// CONSTRUCTOR
	public AllStock() {
		users= null;
		//Login Default
		users = new Admin("1234", "admin", "CC", "admin", User.ADMINISTRADOR);
		
		companies = null;
		companies = new Company("CHOCOLIXIE", "121212", "CALI", "3558788", "Alimentos");
		actualCompany = null;
	}

	

	
	
	public void addCompanyList(String name, String nit, String locate, String phone, String category) {
		
		Company nuevo = new Company(name, nit, locate, phone, category);
		if (actualCompany == null) {
			actualCompany = nuevo;
		}else {
			Company aux = actualCompany;
			while (aux.getNextCompany() != null) {
				aux = aux.getNextCompany();
			}
			nuevo.setPrevCompany(aux);
			aux.setNextCompany(nuevo);
		}

	}
	
	//eliminar compania recursiva

	public void DeleteUser(String nit) {
		if (companies != null) {
			deleteCompanyR(nit, companies);
		}

	}

	public void deleteCompanyR(String nit, Company actualCompany) {

		if (actualCompany != null) {

			if (actualCompany.getNit().equals(nit)) {
				actualCompany.getPrevCompany().setNextCompany(actualCompany.getNextCompany());
				actualCompany.getNextCompany().setPrevCompany(actualCompany.getPrevCompany());
			} else {
				deleteCompanyR(nit, actualCompany.getNextCompany());
			}

		}
	}
	
	public void searchCompanyRec(Company actualCompany, String name, String nit) {

		if (actualCompany == null) {
			// empresa no encontrada

			searchCompanyRec(actualCompany.getNextCompany(), "", "");
		}


	if (actualCompany.getName().equalsIgnoreCase(name) || actualCompany.equals(nit)) {

			searchCompanyRec(actualCompany.getNextCompany(), name, nit);
		}
	}
		
		
	
	

	// AGREGAR PRODUCT LO MISMO DE ARRIBA
	public void addProduct(String name, String description, String brand, double price, int cant, double weight,
			String type) throws ValueIsEmptyException, AlreadyProductExistException {
		if (name.isEmpty() || description.isEmpty() || brand.isEmpty() || brand.isEmpty() || type.isEmpty()
				|| price <= 0 || cant < 0) {
			throw new ValueIsEmptyException();
		} else {
			if (searchProductByName(name) == null) {
				String id = generateIdProducts();
				Product nuevo = new Aliments(id, name, description, brand, price, cant, weight, type);
				actualCompany.setCantProducts(actualCompany.getCantProducts()+1);
				actualCompany.getProducts().add(nuevo);
			} else {
				throw new AlreadyProductExistException();
			}
		}
	}

	public void addProduct(String name, String description, String brand, double price, int cant, double[] sizes,
			String[] colors) throws ValueIsEmptyException, AlreadyProductExistException {
		if (name.isEmpty() || description.isEmpty() || brand.isEmpty() || brand.isEmpty() || price <= 0 || cant < 0
				|| sizes.length == 0 || colors.length == 0) {
			throw new ValueIsEmptyException();
		} else {
			if (searchProductByName(name) == null) {
				String id = generateIdProducts();
				Product nuevo = new Clothes(id, name, description, brand, price, cant, sizes, colors);
				actualCompany.setCantProducts(actualCompany.getCantProducts()+1);
				actualCompany.getProducts().add(nuevo);
			} else {
				throw new AlreadyProductExistException();
			}
		}
	}

	public void addProduct(String name, String description, String brand, double price, int cant)
			throws ValueIsEmptyException, AlreadyProductExistException {
		if (name.isEmpty() || description.isEmpty() || brand.isEmpty() || brand.isEmpty() || price <= 0 || cant < 0) {
			throw new ValueIsEmptyException();
		} else {
			if (searchProductByName(name) == null) {
				String id = generateIdProducts();
				Product nuevo = new Cleaning(id, name, description, brand, price, cant);
				actualCompany.setCantProducts(actualCompany.getCantProducts()+1);
				actualCompany.getProducts().add(nuevo);
			} else {
				throw new AlreadyProductExistException();
			}
		}
	}

	public void addProduct(String name, String description, String brand, double price, int cant, String type)
			throws ValueIsEmptyException, AlreadyProductExistException {
		if (name.isEmpty() || description.isEmpty() || brand.isEmpty() || brand.isEmpty() || type.isEmpty()
				|| price <= 0 || cant < 0 || type.isEmpty()) {
			throw new ValueIsEmptyException();
		} else {
			if (searchProductByName(name) == null) {
				String id = generateIdProducts();
				Product nuevo = new Medicines(id, name, description, brand, price, cant, type);
				actualCompany.setCantProducts(actualCompany.getCantProducts()+1);
				actualCompany.getProducts().add(nuevo);
			} else {
				throw new AlreadyProductExistException();
			}
		}
	}

	public void addProducts(String name, String description, String brand, double price, int cant,
			String[][] characteristics) throws ValueIsEmptyException, AlreadyProductExistException {
		if (name.isEmpty() || description.isEmpty() || brand.isEmpty() || brand.isEmpty() || price <= 0 || cant < 0) {
			throw new ValueIsEmptyException();
		} else {
			if (searchProductByName(name) == null) {
				String id = generateIdProducts();
				Product nuevo = new Others(id, name, description, brand, price, cant, characteristics);
				actualCompany.setCantProducts(actualCompany.getCantProducts()+1);
				actualCompany.getProducts().add(nuevo);
			} else {
				throw new AlreadyProductExistException();
			}
		}
	}

	private String generateIdProducts() {
		int aux = 0;
		
		String result = "";
		if (actualCompany != null) {
			aux = actualCompany.getCantProducts();
		}
		if (aux < 10) {
			result = "000" + aux;
		} else if (aux < 100) {
			result = "00" + aux;
		} else if (aux < 1000) {
			result = "0" + aux;
		} else {
			result = "" + aux;
		}
		return result;
	}

	// BUSCAR UN PRODUCTO POR SU NOMBRE
	public Product searchProductByName(String name) {
		Product result = null;
		if(actualCompany.getProducts()!=null) {
			Product current = actualCompany.getProducts();
			while(current!=null&&result==null) {
				if(current.getName().equals(name)) {
					result = current;
				}else {
					if(name.compareToIgnoreCase(current.getName())<1) {
						current = current.getLeft();
					}else {
						current = current.getRight();
					}
				}
			}
		}
		return result;
	}
	//LOS PRODUCTOS ESTAN EN UN ARBOL BINARIO
	// BUSCAR UN PRODUCTO POR SU ID
	public Product searchProductById(String id) {
		Product searched = null;
		if(actualCompany.getProducts()!=null) {
			searched =  searchProductByIdR(id,actualCompany.getProducts());
		}
		return searched;		
	}
	
	private Product searchProductByIdR(String idSearch, Product actual){
		if(actual.getId().equals(idSearch)) {
			return actual;
		}
		Product searched = null;
		if(actual.getLeft()!=null) {
			searched = searchProductByIdR(idSearch,actual.getLeft());
		}
		if(actual.getRight()!=null&&searched==null){
			searched = searchProductByIdR(idSearch,actual.getRight());
		}
		return searched;
	}
	
	
	// AADIR UN USUARIO

	public void addUser(String id, String name, String idType, String password, String userType){
		if (searchUserR(id) == null) {
			User nuevo;
			if (idType.equals(User.ADMINISTRADOR)) {
				nuevo = new Admin(id, name, idType, password, userType);
			} else if (id.equals(User.EMPLOYEE)) {
				nuevo = new Employee(id, name, idType, password, userType);
			} else {
				nuevo = new Client(id, name, idType, password, userType);
			}
			if(users==null) {
				users = nuevo;
			}
			addUserR(nuevo,users);
		}else {
			
			try{
				throw new UserExistException();
			}catch(UserExistException e) {
				
			}
		}

	}
	private void addUserR(User nuevo,User current) {
		if(current.getNext()==null) {
			current.setNext(nuevo);
		}else {
			addUserR(nuevo,current.getNext());
		}
	}
	
	public void delateUserR(String idName) {
		if(users!=null) {
			delateUserR(idName,users);
		}
	}
	
	private void delateUserR(String idName,User current) {
		if(current!=null) {
			if(current.getName().equals(idName)||current.getId().equals(idName)) {
				current.getPrev().setNext(current.getNext());
				current.getNext().setPrev(current.getPrev());
			}else {
				delateUserR(idName,current.getNext());
			}
		}
	}

	public User searchUserR(String idName) {
		User result = searchUserR(users,idName);;
		return result;
	}
	private User searchUserR(User current,String idName) {
		if(current!=null && (!current.getId().equals(idName)&&!current.getName().equals(idName))){
			searchUserR(current.getNext(),idName);
		}
		return current;
	}

	// BORRA TODOS LOS DATOS ACTUALES PERO GUARDA UNA COPIA EN EL ORDENADOR
	public void reset() {

	}

	// SI EXISTE BORRAR UN PRODUCTO, SOLO PARA EMPLEADOS Y ADMIN
	public void delateProduc(String idName) {
		
	}

	// LOGIN
	public boolean loginUser(String id, String password) throws ValueIsEmptyException {
		boolean validate = false;
		if (!id.isEmpty() && !password.isEmpty()) {
			User user = searchUserR(id);
			if(user!=null) {
				String passwordLogin = user.getPassword();
				validate = passwordLogin.equals(password);
			}

		}else {
			throw new ValueIsEmptyException();
		}
		
		return validate;

	}
	
	private User[] generateUserArray() {
		int cant = 0;
		User current = users;
		while(current!=null) {
			cant++;
			current = current.getNext();
		}
		User[] result = new User[cant];
		current = users;
		int i=0;
		while(current!=null) {
			result[i] = current;
			current = current.getNext();
			i++;
		}
		return result;
	}

	public ArrayList<User> userBubbleSortbyName(){
		User[] users = generateUserArray();
		ArrayList<User> result = new ArrayList<User>();
		for (int i = users.length; i >0; i--) {
			for (int j = 0; j < i-1; j++) {
				int compare =users[j].getName().compareTo(users[j+1].getName());
				if(compare<0) {
					User temp = users[j];
					users[j] = users[j+1];
					users[j+1]=temp;
				}else if(compare ==0) {
					 if(Integer.parseInt(users[j].getId())<Integer.parseInt(users[j+1].getId())) {
						User temp = users[j];
						users[j] = users[j+1];
						users[j+1]=temp;
					 }
				}
			}
		}
		for (int i = 0; i < users.length; i++) {
			result.add(users[i]);
		}
		return result;
	}
	
	public ArrayList<User> userSelectionSortById(){
		User[] users = generateUserArray();
		ArrayList<User> result = new ArrayList<User>();
		for (int i = 0; i < users.length; i++) {
			User less =	users[i];
			int wich = i;
			for (int j = i+1; j < users.length; j++) {
				
				if(Integer.parseInt(users[j].getId())<Integer.parseInt(less.getId())) {
					less = users[j];
					wich = j;
				}
			}
			User temp = users[i];
			users[i] = less;
			users[wich] = temp;
			result.add(less);
		}
		return result;
	}

	public ArrayList<Product> productSelectionSortById(Product[] products){
		ArrayList<Product> result = new ArrayList<Product>();
		for (int i = 0; i < products.length; i++) {
			Product less =	products[i];
			int wich = i;
			for (int j = i+1; j < products.length; j++) {
				
				if(Integer.parseInt(products[j].getId())<Integer.parseInt(less.getId())) {
					less = products[j];
					wich = j;
				}
			}
			Product temp = products[i];
			products[i] = less;
			products[wich] = temp;
			result.add(less);
		}
		return result;
	}
	
	public ArrayList<Product> productInsertionSortByName(Product[] products){
		ArrayList<Product> result = new ArrayList<Product>();
		for (int i = 1; i < products.length; i++) {
		
			for (int j = i; j >0 && products[j-1].getName().compareToIgnoreCase(products[j].getName())>=0; j--) {
				if(products[j-1].getName().compareToIgnoreCase(products[j].getName())==0) {
					if(Integer.parseInt(products[j-1].getId())>Integer.parseInt(products[j].getId())) {
						Product temp = products[j];
						products[i] = products[j-1];
						products[j-1] = temp;
					}
				}else {
					Product temp = products[j];
					products[i] = products[j-1];
					products[j-1] = temp;
				}
			}
		}
		return result;
	}
	
	// GENEERA UN REPORTE Y LOS GUARDA EN UN ARCHIVO TXT, EL DIRECTORIO ES UNA
	// CONSTANTE
	public void generateReportUsers() throws IOException {
		File file = new File("data/reports/users.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		String report = "id type;Nombre;id";
		User current = users;
		while(current!=null) {
			String id = current.getId();
			String name = current.getName();
			String idType = current.getIdType();

			report += "\n" + idType + ";" + name + ";" + id;
			current = current.getNext();
		}
		bw.write(report);
		bw.close();
	}

	public void generateIncreasesDecreases() {

	}

	public void generateEarningsLoses() {

	}

	// Genera y retorna los datos necesarios para las graficas de la GUI
	public void generateGraphics() {

	}

}