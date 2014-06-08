class My
{
	int x;
	int y;
public:
	void set(int i) { this->x = i; }
	int get(void) { return this->x; }
};

class Their: public My
{
    int x;
    int y;

    public:

    void setT(int i) { this->x = i;}
    int getT(void) {return this->x;}
};

int main()
{
	class My *m = new My();
	class Their *t = new Their();
	m->set(100);
	int x = m->get();

	t->setT(300);
	int y = t->getT();
}