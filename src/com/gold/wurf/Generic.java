package com.gold.wurf;

/* ���������� ����� */

public class Generic <T, P> implements InterfaceExample, Cloneable{

// ������� ����� T � P
private T myobject1;
private P myobject2;

	// �����������
	Generic(T obj1, P obj2){
		this.setMyobjects(obj1, obj2);
	}

	// ������� myobject1
	public T getMyobject1() {
		return myobject1;
	}
	// ������� myobject2
	public P getMyobject2() {
		return myobject2;
	}

	// ���������� myobject1 � myobject2
	public void setMyobjects(T obj1, P obj2) {
		this.myobject1 = obj1;
		this.myobject2 = obj2;
	}

	// ����� ���������� ���� T � P, ������� ������� �������� ������ Generic � MainActivity
	public String ShowTypes(){
		return myobject1.getClass().getName() + " " + myobject2.getClass().getName();
	}

	// ���������� ����������� �����
	static class A {
		// ������� ��������� ������� ������� obj[]
		// ... ���������� ���������� ���-�� ����������, ��������� ������ T[]. �������� c ... ������ ���� ������������ � ������ ���������!
		public static <T> T getObj(T... obj) { return obj[obj.length-1]; }
	}

	// ������ ���������� InterfaceExample
	@Override
	public String getInterfaceName() {
		// TODO Auto-generated method stub
		return "InterfaceExample getName() � getAge() "+getCount()+" "+Wurf;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 25;
	}

	// ������������ ������� (�������������� ����� clone() �� ���������� Cloneable)
	public Object clone() {
		try {
			return super.clone(); // ����� �������� ������
		}
		catch (CloneNotSupportedException e) {
			throw new AssertionError("����������!");
		}
	}
}
