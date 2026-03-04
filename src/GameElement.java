public abstract class GameElement
{
    private final int id;
    private String name;
    public GameElement(int id, String name)
    {
        this.id = id;
        this.name = name;
    }
    public int getElemId()
    {
        return id;
    }
    public String getElemName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getData()
    {
        return "id: " +  id + ", name: " + name;
    }
}
