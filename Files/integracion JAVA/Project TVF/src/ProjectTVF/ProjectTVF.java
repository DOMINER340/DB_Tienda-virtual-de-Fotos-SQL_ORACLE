/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjectTVF;

import GUI.*;
import controllers.*;
import entities.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Temporal;
import javax.swing.ImageIcon;

/**
 *
 * @author Gustavo
 */
public class ProjectTVF {

    private static menu pantallaMenu;

    //______________________________________________________
    //estamos asumiendo que cierto usuario inicio sesion correctamente.
    //y ahora esta iniciando el proceso de buscar fotos para continuar segun lo establecido en el enunciado.
    //________________________________________________________
    
    //el ID de este usuario que inicio sesion previamente es "AlvaroQ"
    private static final String USUARIO_sesion = "AlvaroQ";
    
    // ID carrito de compras temporal 
    private String ID_carro_temp = null;
    
    private final photoJpaController ControPhoto;
    private final ptvfJpaController ControPtvf;
    private final accountJpaController ControAccounts;
    private final characterJpaController ControCharater;
    private final countryJpaController ControCountry;
    private final creditPaymentFormatJpaController ControCreditPaymentFormat;
    private final discountJpaController ControDiscounts;
    private final eventJpaController ControEvent;
    private final formatSelectJpaController ControFormatSelect;
    private final phxvaJpaController ControIntermediate_Phxv;
    private final linkJpaController ControLink;
    private final paypalJpaController ControPayPal;
    private final purchaseJpaController ControPurchase;
    private final seasonJpaController ControSeason;
    private final shoppingCartJpaController ControShoppingCart;
    private final statsVisitorJpaController ControStatsVisitors;
    private final taxesJpaController ControTaxes;
    private final topicJpaController ControTopic;
    private final bankJpaController ControBank;
    private final EntityManagerFactory EMF;
    
    
    public static void main(String[] args) {
        

        // TODO code application logic here
        ProjectTVF ControladorMain = new ProjectTVF();
        pantallaMenu = new menu();
        pantallaMenu.setVisible(true);
        
        pantallaMenu.setControladorMain(ControladorMain);

    }
    
      public ArrayList<ArrayList<Object>> desplegarLista()
    {
        ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
        BufferedImage ImagenFinal =null;
        ImageIcon icon = null;
        try {
            EntityManager em = getEntityManager();    
             
            Image image = null;
            em.getTransaction().begin();
            java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
            
            Statement stmt = conn.createStatement();
            
            //NUEVA CONSULTA PARA PH
            String query = "SELECT * " + 
                            "FROM PH P left outer join C " +
                            "ON P.C_ID = C.ID";
            stmt.execute(query);
            ResultSet rs = stmt.getResultSet();
            while (rs.next())
            {
                //MODIFICAR LOS ATRIBUTOS QUE AHORA CORRESPONDAN A PH
                
                String tempName = rs.getNString("NAME");
                Blob aBlob = rs.getBlob("PIMAGE");
                String tempDateCreate = rs.getNString("DATE_CREATE");
                String tempDescription = rs.getNString("DESCRIPTION");
                
                //se ubica el numero de la ultima columna, la cual guarda el nombre del pais
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                
                //se pasa el entero anteriormente encontrado, para obtener el valor de la ultima columna
                String tempString = rs.getNString(columnsNumber);                
                
                //System.out.print("\n \n aaaaaaaaaaalv estoy por aqui...... n:  "+ aBlob.length() + " --ID:" + tempName + "\n" + "--x: " );
                
                InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                
                BufferedImage imag=ImageIO.read(is);
               
                image =  imag;
                icon =new ImageIcon(image);           
                
                ArrayList<Object> singleList = new ArrayList<Object>();
                
                singleList.add(tempName);
                singleList.add(icon);
                singleList.add(tempDescription);
                singleList.add(tempDateCreate);
                singleList.add(tempString);
                
            
                listOLists.add(singleList);
            }
           
            em.getTransaction().commit();

       } catch (SQLException | IOException ex) {
           Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
       }
       return listOLists;
    }
      
      
      //
      //
      //
      //
      //posiblement aqui falta pasar el valor de l a persistencia, algo como Parcial_FinalPU
      //
    public EntityManager getEntityManager()
    {
        
        EntityManager manager = EMF.createEntityManager();
        return manager;
    }
    
    
//    public List<String> getListLocationNames()
//    {
//        //no tenemos una entidad LOCATION por que lo definimos
//        //como un valor muy especifico que no se repetiria
//        return null;
//    }
          
    public List<String> getListCountryNames()
    {
        List<country> TempCountries = ControCountry.findcountryEntities();
        List<String> TempC;
        TempC = new ArrayList<String>();
        for (country Temp : TempCountries)
        {
            TempC.add(Temp.getName());
            
        }
        return TempC;
    }

        
    public List<String> getListEventNames()
    {
        List<event> TempEvent = ControEvent.findeventEntities();
        List<String> TempE;
        TempE = new ArrayList<String>();
        for (event Temp : TempEvent)
        {
            TempE.add(Temp.getName());
        }
        return TempE;
    }
        
    public List<String> getListCharacterNames()
    {
        List<character> TempCharacter = ControCharater.findcharacterEntities();
        List<String> TempCH;
        TempCH = new ArrayList<String>();
        for (character Temp : TempCharacter)
        {
            TempCH.add(Temp.getName());
        }
        return TempCH;
    }
        
    public List<String> getListTopicNames()
    {    
        List<topic> TempTopic = ControTopic.findtopicEntities();
        List<String> TempT;
        TempT = new ArrayList<String>();
        for (topic Temp : TempTopic)
        {
            TempT.add(Temp.getName());
        }
        return TempT;
    }
        
    
    bankJpaController getBankJpaController()
    {
        return this.ControBank;
    }
    public ProjectTVF()
    {
        EMF = Persistence.createEntityManagerFactory("Project_TVFPU");
        
        ControBank = new bankJpaController(EMF);
        ControPhoto = new photoJpaController(EMF);
        ControPtvf = new ptvfJpaController(EMF);
        ControAccounts = new accountJpaController(EMF);        
        ControCharater = new characterJpaController(EMF);
        ControCountry = new countryJpaController(EMF);
        ControCreditPaymentFormat = new creditPaymentFormatJpaController(EMF);
        ControDiscounts = new discountJpaController(EMF);
        ControEvent = new eventJpaController(EMF);
        ControFormatSelect = new formatSelectJpaController(EMF);
        ControIntermediate_Phxv = new phxvaJpaController (EMF);
        ControLink = new linkJpaController (EMF);
        ControPayPal = new paypalJpaController (EMF);
        ControPurchase = new purchaseJpaController (EMF);
        ControSeason = new seasonJpaController (EMF);
        ControShoppingCart = new shoppingCartJpaController (EMF);
        ControStatsVisitors = new statsVisitorJpaController (EMF);
        ControTaxes = new taxesJpaController (EMF);
        ControTopic = new topicJpaController (EMF); 
    }

    public bankJpaController getControBank() {
        return ControBank;
    }

    public photoJpaController getControPhoto() {
        return ControPhoto;
    }

    public ptvfJpaController getControPtvf() {
        return ControPtvf;
    }

    public accountJpaController getControAccounts() {
        return ControAccounts;
    }

    public characterJpaController getControCharater() {
        return ControCharater;
    }

    public countryJpaController getControCountry() {
        return ControCountry;
    }

    public creditPaymentFormatJpaController getControCreditPaymentFormat() {
        return ControCreditPaymentFormat;
    }

    public discountJpaController getControDiscounts() {
        return ControDiscounts;
    }

    public eventJpaController getControEvent() {
        return ControEvent;
    }

    public formatSelectJpaController getControFormatSelect() {
        return ControFormatSelect;
    }

    public phxvaJpaController getControIntermediate_Phxv() {
        return ControIntermediate_Phxv;
    }

    public linkJpaController getControLink() {
        return ControLink;
    }

    public paypalJpaController getControPayPal() {
        return ControPayPal;
    }

    public purchaseJpaController getControPurchase() {
        return ControPurchase;
    }

    public seasonJpaController getControSeason() {
        return ControSeason;
    }

    public shoppingCartJpaController getControShoppingCart() {
        return ControShoppingCart;
    }

    public statsVisitorJpaController getControStatsVisitors() {
        return ControStatsVisitors;
    }

    public taxesJpaController getControTaxes() {
        return ControTaxes;
    }

    public topicJpaController getControTopic() {
        return ControTopic;
    }

    public void desplegarListaXPaisSelect(String TempText) {
        
        ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
        ImageIcon icon = null;
            
        List<photo> PhotoTemp = ControPhoto.findphotoEntities();
        
        System.out.print("\n  PhotoTemp X:_" + PhotoTemp.size()+ "_\n" );

        for (photo temp : PhotoTemp)
        {
            ArrayList<Object> ListTemp = null;
            ListTemp = new ArrayList<Object>();
            System.out.print(" __MIRAME X:_" + temp.getCId().getName().toUpperCase().toString() + "_A_" + TempText.trim().toUpperCase() + "__ " );
            if (temp.getCId().getName().toUpperCase().toString().equals(TempText.trim().toUpperCase()) )
            {   
                System.out.print(" entre " );
                ListTemp.add(temp.getName());
                ListTemp.add(icon);
                
                ListTemp.add(temp.getDescription());
                ListTemp.add(temp.getDateCreate());
                ListTemp.add(temp.getCId().getName());
                listOLists.add(ListTemp);
            }
            else
            {
                //abrir ventana que diga que la busqueda no arrojo resultados
            }  
        }  
        System.out.print("\n \n MIRAME sizeList:_" + listOLists.size() + "_" );
        if(listOLists.size() > 0)
        {
            ArrayList<Object> listTemp = new ArrayList<Object>();
            try {
                EntityManager em = getEntityManager();    
                
                Image image = null;
                em.getTransaction().begin();
                java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                
                Statement stmt = conn.createStatement();
                
                //NUEVA CONSULTA PARA PH
                String query =  null;
                query =     "SELECT * " + 
                            "FROM PH P left outer join C " +
                            "ON P.C_ID = C.ID WHERE C.NAME = " + "'" + TempText.trim() + "'" ;
                
                stmt.execute(query);
                ResultSet rs = stmt.getResultSet();
                System.out.print("\n columnas___ " +  rs.getMetaData().getColumnCount() +"___ \n \n" );
                
                
                while (rs.next())
                {
                    System.out.print("\n NAME RS:___ " +  rs.getNString("NAME") +"___ \n \n" );
                    Blob aBlob = rs.getBlob("PIMAGE");
                    InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                    
                    BufferedImage imag=ImageIO.read(is);
                    
                    image =  imag;
                    icon =new ImageIcon(image);  
                    listTemp.add(icon);
                    
                }
                System.out.print("\nMIRAME listTemp:_" + listTemp.size() + "_  \n \n" );
                em.getTransaction().commit();

            }
            catch (SQLException | IOException ex) {
               Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
            }     
            
            //IF LISTOFLIST = UNLL ENTONCES DESPLEGAR TODAS LAS FOTOS
            
            //aqui se reemplaza el icono por el de la imagen anteriormente obtenida
            int count = 0;
            for (ArrayList<Object> Temp : listOLists)
            {
                Temp.set(1, listTemp.get(count));
                count ++;
            }
            pantallaMenu.VerFotos(listOLists);
            System.out.print("\n \n MIRAME : " + listOLists.size() + "  \n \n" );
        }
        else
        {
            pantallaMenu.mostrarDialogoBusquedaNoResultados();
        }
       //return listOLists;
    }
    
    public void desplegarListaXEventSelect(String TempText) {
        
        ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
        ImageIcon icon = null;
            
        List<photo> PhotoTemp = ControPhoto.findphotoEntities();
        
        System.out.print("\n  PhotoTemp X:_" + PhotoTemp.size()+ "_\n" );

        for (photo temp : PhotoTemp)
        {
            ArrayList<Object> ListTemp = null;
            ListTemp = new ArrayList<Object>();
            
            if(temp.getEId() != null)
            {
                System.out.print(" __MIRAME X:_" + temp.getEId().getName() + "_A_" + TempText.trim().toUpperCase() + "__ " );
                if (  temp.getEId().getName().toUpperCase().equals( TempText.trim().toUpperCase() ) )
                {   
                    System.out.print(" entre " );
                    ListTemp.add(temp.getName());
                    ListTemp.add(icon);
                
                    ListTemp.add(temp.getDescription());
                    ListTemp.add(temp.getDateCreate());
                    ListTemp.add(temp.getCId().getName());
                    listOLists.add(ListTemp);
                }
                else
                {
                    //abrir ventana que diga que la busqueda no arrojo resultados
                }   
            }
            
        }  
        System.out.print("\n \n MIRAME sizeList:_" + listOLists.size() + "_" );
        if(listOLists.size() > 0)
        {
            ArrayList<Object> listTemp = new ArrayList<Object>();
            try {
                EntityManager em = getEntityManager();    
                
                Image image = null;
                em.getTransaction().begin();
                java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                
                Statement stmt = conn.createStatement();
                
                //NUEVA CONSULTA PARA PH
                String query =  null;
                query =     "SELECT * " + 
                            "FROM PH P left outer join E " +
                            "ON P.E_ID = E.ID WHERE E.NAME = " + "'" + TempText.trim() + "'" ;
                
                stmt.execute(query);
                ResultSet rs = stmt.getResultSet();
                System.out.print("\n columnas___ " +  rs.getMetaData().getColumnCount() +"___ \n \n" );
                
                while (rs.next())
                {
                    System.out.print("\n NAME RS:___ " +  rs.getNString("NAME") +"___ \n \n" );
                    Blob aBlob = rs.getBlob("PIMAGE");
                    InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                    
                    BufferedImage imag=ImageIO.read(is);
                    
                    image =  imag;
                    icon =new ImageIcon(image);  
                    listTemp.add(icon);  
                }
                System.out.print("\nMIRAME listTemp:_" + listTemp.size() + "_  \n \n" );
                em.getTransaction().commit();

            }
            catch (SQLException | IOException ex) {
               Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
            }     
            
            //IF LISTOFLIST = UNLL ENTONCES DESPLEGAR TODAS LAS FOTOS
            
            //aqui se reemplaza el icono por el de la imagen anteriormente obtenida
            int count = 0;
            for (ArrayList<Object> Temp : listOLists)
            {
                Temp.set(1, listTemp.get(count));
                count ++;
            }
            pantallaMenu.VerFotos(listOLists);
            System.out.print("\n \n MIRAME : " + listOLists.size() + "  \n \n" );
        }
        else
        {
            pantallaMenu.mostrarDialogoBusquedaNoResultados();
        }
       //return listOLists;
    }
    
     public void desplegarListaXCharacterSelect(String TempText) {
        
        ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
        ImageIcon icon = null;
            
        List<photo> PhotoTemp = ControPhoto.findphotoEntities();
        
        System.out.print("\n  PhotoTemp X:_" + PhotoTemp.size()+ "_\n" );

        for (photo temp : PhotoTemp)
        {
            ArrayList<Object> ListTemp = null;
            ListTemp = new ArrayList<Object>();
            
            if(temp.getChId() != null)
            {
                System.out.print(" __MIRAME X:_" + temp.getChId().getName() + "_A_" + TempText.trim().toUpperCase() + "__ " );
                if (  temp.getChId().getName().toUpperCase().equals( TempText.trim().toUpperCase() ) )
                {   
                    System.out.print(" entre " );
                    ListTemp.add(temp.getName());
                    ListTemp.add(icon);
                
                    ListTemp.add(temp.getDescription());
                    ListTemp.add(temp.getDateCreate());
                    ListTemp.add(temp.getCId().getName());
                    listOLists.add(ListTemp);
                }
                else
                {
                    //abrir ventana que diga que la busqueda no arrojo resultados
                }   
            }
            
        }  
        System.out.print("\n \n MIRAME sizeList:_" + listOLists.size() + "_" );
        if(listOLists.size() > 0)
        {
            ArrayList<Object> listTemp = new ArrayList<Object>();
            try {
                EntityManager em = getEntityManager();    
                
                Image image = null;
                em.getTransaction().begin();
                java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                
                Statement stmt = conn.createStatement();
                
                //NUEVA CONSULTA PARA PH
                String query =  null;
                query =     "SELECT * " + 
                            "FROM PH P left outer join CH " +
                            "ON P.CH_ID = CH.ID WHERE CH.NAME = " + "'" + TempText.trim() + "'" ;
                
                stmt.execute(query);
                ResultSet rs = stmt.getResultSet();
                System.out.print("\n columnas___ " +  rs.getMetaData().getColumnCount() +"___ \n \n" );
                
                while (rs.next())
                {
                    System.out.print("\n NAME RS:___ " +  rs.getNString("NAME") +"___ \n \n" );
                    Blob aBlob = rs.getBlob("PIMAGE");
                    InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                    
                    BufferedImage imag=ImageIO.read(is);
                    
                    image =  imag;
                    icon =new ImageIcon(image);  
                    listTemp.add(icon);  
                }
                System.out.print("\nMIRAME listTemp:_" + listTemp.size() + "_  \n \n" );
                em.getTransaction().commit();

            }
            catch (SQLException | IOException ex) {
               Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
            }     
            
            //IF LISTOFLIST = UNLL ENTONCES DESPLEGAR TODAS LAS FOTOS
            
            //aqui se reemplaza el icono por el de la imagen anteriormente obtenida
            int count = 0;
            for (ArrayList<Object> Temp : listOLists)
            {
                Temp.set(1, listTemp.get(count));
                count ++;
            }
            pantallaMenu.VerFotos(listOLists);
            System.out.print("\n \n MIRAME : " + listOLists.size() + "  \n \n" );
        }
        else
        {
            pantallaMenu.mostrarDialogoBusquedaNoResultados();
        }
       //return listOLists;
    }
     
      public void desplegarListaXTopicSelect(String TempText) {
        
        ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
        ImageIcon icon = null;
            
        List<photo> PhotoTemp = ControPhoto.findphotoEntities();
        
        System.out.print("\n  PhotoTemp X:_" + PhotoTemp.size()+ "_\n" );

        for (photo temp : PhotoTemp)
        {
            ArrayList<Object> ListTemp = null;
            ListTemp = new ArrayList<Object>();
            
            if(temp.getTopicId() != null)
            {
                System.out.print(" __MIRAME X:_" + temp.getTopicId().getName() + "_A_" + TempText.trim().toUpperCase() + "__ " );
                if (  temp.getTopicId().getName().toUpperCase().equals( TempText.trim().toUpperCase() ) )
                {   
                    System.out.print(" entre " );
                    ListTemp.add(temp.getName());
                    ListTemp.add(icon);
                
                    ListTemp.add(temp.getDescription());
                    ListTemp.add(temp.getDateCreate());
                    ListTemp.add(temp.getCId().getName());
                    listOLists.add(ListTemp);
                }
                else
                {
                    //abrir ventana que diga que la busqueda no arrojo resultados
                }   
            }
            
        }  
        System.out.print("\n \n MIRAME sizeList:_" + listOLists.size() + "_" );
        if(listOLists.size() > 0)
        {
            ArrayList<Object> listTemp = new ArrayList<Object>();
            try {
                EntityManager em = getEntityManager();    
                
                Image image = null;
                em.getTransaction().begin();
                java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                
                Statement stmt = conn.createStatement();
                
                //NUEVA CONSULTA PARA PH
                String query =  null;
                query =     "SELECT * " + 
                            "FROM PH P left outer join TOPIC T " +
                            "ON P.TOPIC_ID = T.ID WHERE T.NAME = " + "'" + TempText.trim() + "'" ;
                
                stmt.execute(query);
                ResultSet rs = stmt.getResultSet();
                System.out.print("\n columnas___ " +  rs.getMetaData().getColumnCount() +"___ \n \n" );
                
                while (rs.next())
                {
                    System.out.print("\n NAME RS:___ " +  rs.getNString("NAME") +"___ \n \n" );
                    Blob aBlob = rs.getBlob("PIMAGE");
                    InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                    
                    BufferedImage imag=ImageIO.read(is);
                    
                    image =  imag;
                    icon =new ImageIcon(image);  
                    listTemp.add(icon);  
                }
                System.out.print("\nMIRAME listTemp:_" + listTemp.size() + "_  \n \n" );
                em.getTransaction().commit();

            }
            catch (SQLException | IOException ex) {
               Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
            }     
            
            //aqui se reemplaza el icono por el de la imagen anteriormente obtenida
            int count = 0;
            for (ArrayList<Object> Temp : listOLists)
            {
                Temp.set(1, listTemp.get(count));
                count ++;
            }
            pantallaMenu.VerFotos(listOLists);
            System.out.print("\n \n MIRAME : " + listOLists.size() + "  \n \n" );
        }
        else
        {
            pantallaMenu.mostrarDialogoBusquedaNoResultados();
        }
       //return listOLists;
    }

      
      
    public void desplegarListaXVariable(String Country, String Topic, String Character, String Event) {
        ArrayList<ArrayList<Object>> listOLists = new ArrayList<ArrayList<Object>>();
        ImageIcon icon = null;
            
        List<photo> Photo = ControPhoto.findphotoEntities();
        List<photo> PhotoTemp = Photo;
        
        System.out.print("\n  PhotoTemp X:_" + PhotoTemp.size()+ "_\n" );

        //ahora con la lista se filtra para saber que fotos cumplen no solo con uno de los parametros, si no con todos
        for (photo temp : PhotoTemp)
        { 
            ArrayList<Object> ListTemp = null;
            ListTemp = new ArrayList<Object>();
            
            boolean banderaCountry = false;
            boolean banderaTopic = false;
            boolean banderaCharacter = false;
            boolean banderaEvent = false;
            
            //se busca el atributo del country o si es null
            if(Country == null)
            {
                banderaCountry = true;
            }
            else
            {
                if (temp.getCId()!= null )
                {
                    if( temp.getCId().getName().equals(Country) ) { banderaCountry = true; }
                }
            }
            
            //se busca el atributo del topic o si es null
            if(Topic == null)
            {
                banderaTopic = true;
            }
            else
            {
                if (temp.getTopicId()!= null )
                {
                    if( temp.getTopicId().getName().equals(Topic) ) { banderaTopic = true; }
                }
            }
            
            //se busca el atributo del Character o si es null
            if(Character == null)
            {
                banderaCharacter = true;
            }
            else
            {
                if (temp.getChId()!= null )
                {
                    if( temp.getChId().getName().equals(Character) ) { banderaCharacter = true; }
                }
            }
            
            //se busca el atributo del Event o si es null
            if(Event == null)
            {
                banderaEvent = true;
            }
            else
            {
                if (temp.getEId()!= null )
                {
                    if( temp.getEId().getName().equals(Event) ) { banderaEvent = true; }
                }
            }
        
            //ahora se filtra para saber si la foto cumple con todas las condiciones y si lo hace, se añade a una lista
            if  (
                    ( banderaCountry ) &&
                    ( banderaTopic ) &&
                    ( banderaCharacter ) &&
                    ( banderaEvent )
                )
            {
                ListTemp.add(temp.getName());
                ListTemp.add(icon);
                
                ListTemp.add(temp.getDescription());
                ListTemp.add(temp.getDateCreate());
                ListTemp.add(temp.getCId().getName());
                listOLists.add(ListTemp);
            }
        }

        
        System.out.print("\n \n MIRAME sizeList:_" + listOLists.size() + "_" );
        if(listOLists.size() > 0)
        {
            ArrayList<Object> listTemp = new ArrayList<Object>();
            try {
                
                
                EntityManager em = getEntityManager();    
                
                Image image = null;
                em.getTransaction().begin();
                java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                
                Statement stmt = conn.createStatement();
                
                //NUEVA CONSULTA PARA PH
                String query =  null;
                query =     "SELECT * " + 
                            "FROM PH " ;
                
                stmt.execute(query);
                ResultSet rs = stmt.getResultSet();
                System.out.print("\n columnas___ " +  rs.getMetaData().getColumnCount() +"___ \n \n" );
                
                while (rs.next())
                {
                    int count = 0;
                    for (ArrayList<Object> temp : listOLists)
                    {
                        String temporal = (String) temp.get(0);
                        if( temporal.equals( rs.getNString("NAME") ) )
                        {
                            System.out.print("\n NAME RS:___ " +  rs.getNString("NAME") +"___ \n \n" );
                            Blob aBlob = rs.getBlob("PIMAGE");
                            InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                            
                            BufferedImage imag=ImageIO.read(is);
                    
                            image =  imag;
                            icon =new ImageIcon(image);  
                            
                            temp.set(1, icon);
                        }
                        count ++;
                    }
                    
                    //listTemp.add(icon);  
                }
                System.out.print("\nMIRAME lsitoflistInside:_" + listOLists.size() + "_  \n \n" );
                em.getTransaction().commit();
            }
            catch (SQLException | IOException ex) {
               Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
            }     
            
            System.out.print("\n \n MIRAME : " + listOLists.size() + "  \n \n" );
            pantallaMenu.VerFotos(listOLists);
            
        }
        else
        {
            pantallaMenu.mostrarDialogoBusquedaNoResultados();
        }
       //return listOLists;
    }

    public ArrayList<Object> traerFotoSeleccionada(String tempString)
    {
        photo Photo = ControPhoto.findphoto(tempString);
        photo PhotoTemp = Photo;
        ImageIcon icon = null;
        ArrayList<Object> ListTemp = null;
        ListTemp = new ArrayList<Object>();
        
        ListTemp.add(PhotoTemp.getName());
        ListTemp.add(icon);
        ListTemp.add(PhotoTemp.getDescription());
        ListTemp.add(PhotoTemp.getDateCreate());
        ListTemp.add(PhotoTemp.getCId().getName());
        
        try {
                
            Image image = null;    
            EntityManager em = getEntityManager();    
            em.getTransaction().begin();
            java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                
            Statement stmt = conn.createStatement();
                
            //NUEVA CONSULTA PARA PH
            String query =  null;
            query =     " SELECT * " + 
                        " FROM PH P " + 
                        " WHERE P.NAME = '" +  tempString.trim() + "' ";
                
            stmt.execute(query);
            ResultSet rs = stmt.getResultSet();
            System.out.print("\n columnas___ " +  rs.getMetaData().getColumnCount() +"___ \n \n" );
                
            while (rs.next())
            {
                    String X = (String) ListTemp.get(0);
                    if( X.equals( rs.getNString("NAME") ) )
                    {
                        System.out.print("\n NAME RS:___ " +  rs.getNString("NAME") +"___ \n \n" );
                        Blob aBlob = rs.getBlob("PIMAGE");
                        InputStream is = aBlob.getBinaryStream(1, aBlob.length());
                        
                        BufferedImage imag=ImageIO.read(is);
                  
                        image =  imag;
                        icon =new ImageIcon(image);  
                           
                        ListTemp.set(1, icon);
                    }                
            }
            System.out.print("\nMIRAME lsitoflistInside:_" + ListTemp.size() + "_  \n \n" );
            em.getTransaction().commit();
        }
        catch (SQLException | IOException ex) {
            Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ListTemp;
    }

    
   
    public boolean agregarAlCarrito(String tempString) 
    {
        
        String ID_carrito = null;
        String Usuariotem;
        EntityManager em = getEntityManager();
        
        boolean bandera_primaria = false;
             
        if(ID_carro_temp == null )
        {
            try {
                em.getTransaction().begin();
                java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
                //debo buscar el id del ultimo carrito de compras creado para el usuario,
                //si no tiene, entonces se le creara uno con el id de 1
                Statement stmt = conn.createStatement();
                Usuariotem = USUARIO_sesion;
                ID_carrito = null;
            
                
                int tamaño = ControShoppingCart.findshoppingCartEntities().size();
                if( tamaño != 0  )
                {
                    //si tiene compras previas pasara esto
                    ID_carrito = Integer.toString( tamaño + 1 );
                    
                }
                else
                {
                    ID_carrito = Integer.toString(1);
                    //aqui se deberia decir que esera el primer carrito de compras y su id sera de 1
                }
                System.out.print("\n \n tamaño? ___" + tamaño + "___ \n \n");
                System.out.print("\n \n ID_caaaaaaaaaaaaarrito ___" + ID_carrito + "___ \n \n");
                
                String query =  null;
                query = " INSERT INTO SC " + 
                        " VALUES ( " + " '" + ID_carrito + "' " + " , "  + " '" + Usuariotem.trim() + "' " + " )";
                stmt.execute(query);
                
                ID_carro_temp = ID_carrito;
                System.out.print("\n \nSE AH CREADO UN CARRITO CON ID ___" + ID_carrito + "___ \n \n");
                em.getTransaction().commit();
                
            } catch (SQLException ex) {
                Logger.getLogger(ProjectTVF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            ID_carrito = ID_carro_temp;
            Usuariotem = USUARIO_sesion;
        }
        
        shoppingCart carrito = ControShoppingCart.findshoppingCart(ID_carrito);
        em.getTransaction().begin();
        java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
        if( carrito.getPhotoCollection().isEmpty() == false )
        {
            //si existen fotos añadidas, se buscara que la nueva no esta incluida, para evitar repeticiones
            Collection<photo> collection;
            collection = carrito.getPhotoCollection();
            
            boolean tempF = false;
            
            for (photo temp : collection)
            {
                if ( temp.getName().equalsIgnoreCase(tempString) )
                {
                    tempF = true;
                }
            }
            
            if(tempF == false)
            {
                
                bandera_primaria = carrito.getPhotoCollection().add( ControPhoto.findphoto(tempString) );
                ControShoppingCart.findshoppingCart(ID_carrito).setPhotoCollection(collection);
                carrito.setPhotoCollection(collection);
                em.getTransaction().commit();
                
            }
        }
        else
        {
            // crear insert aqui
            bandera_primaria = ControShoppingCart.findshoppingCart(ID_carrito).getPhotoCollection().add( ControPhoto.findphoto(tempString) );
            em.getTransaction().commit();
        }        
        return bandera_primaria;
    }
}