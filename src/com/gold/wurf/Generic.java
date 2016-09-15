package com.gold.wurf;

/* Обобщённый класс */

public class Generic <T, P> implements InterfaceExample, Cloneable{

// Объекты типов T и P
private T myobject1;
private P myobject2;

	// Конструктор
	Generic(T obj1, P obj2){
		this.setMyobjects(obj1, obj2);
	}

	// Вернуть myobject1
	public T getMyobject1() {
		return myobject1;
	}
	// Вернуть myobject2
	public P getMyobject2() {
		return myobject2;
	}

	// Установить myobject1 и myobject2
	public void setMyobjects(T obj1, P obj2) {
		this.myobject1 = obj1;
		this.myobject2 = obj2;
	}

	// Метод возвращает типы T и P, объекты которых переданы классу Generic в MainActivity
	public String ShowTypes(){
		return myobject1.getClass().getName() + " " + myobject2.getClass().getName();
	}

	// Внутренний статический класс
	static class A {
		// Выводим последний элемент массива obj[]
		// ... обозначает переменное кол-во параметров, идентично записи T[]. Параметр c ... должен быть единственным и стоять последним!
		public static <T> T getObj(T... obj) { return obj[obj.length-1]; }
	}

	// Методы интерфейса InterfaceExample
	@Override
	public String getInterfaceName() {
		// TODO Auto-generated method stub
		return "InterfaceExample getName() и getAge() "+getCount()+" "+Wurf;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 25;
	}

	// Клонирование объекта (переопределяем метод clone() из интерфейса Cloneable)
	public Object clone() {
		try {
			return super.clone(); // вызов базового метода
		}
		catch (CloneNotSupportedException e) {
			throw new AssertionError("невозможно!");
		}
	}
}
