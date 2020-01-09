package com.fahad.appnavigationdrawer.Controlleurs.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fahad.appnavigationdrawer.Controlleurs.fragments.Alimentaire_Fragment;
import com.fahad.appnavigationdrawer.Controlleurs.fragments.Maison_Decoration_Fragment;
import com.fahad.appnavigationdrawer.Controlleurs.fragments.Mode_Beaute_Fragment;
import com.fahad.appnavigationdrawer.Controlleurs.fragments.Multimedia_Fragment;
import com.fahad.appnavigationdrawer.Controlleurs.fragments.Sport_Loisir_Fragment;
import com.fahad.appnavigationdrawer.R;
import com.fahad.appnavigationdrawer.Utils.SQLiteHandler;
import com.fahad.appnavigationdrawer.Utils.SessionManager;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.HashMap;

/**
 * L'activité principal implements l'interface OnNaviagtionItemSelectedListener qui permet de gerer le menu du
 * Navigation Drawer,
 *
 * Elle implemente aussi l'interface OnButtonClikedListener du fragment TicketFragment
 * qui lui permet gere de lancer l'activité ListeSupermarché_Activity lorque l'utilisateur choisira une ville
 * dans la boite de dialogue des Villes
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
         {

    private NavigationView mNavigationView; //variable qui contiendra la naviagtion View
    private Toolbar mToolbar;// variable contiendra le Toolbar
    private DrawerLayout mDrawerLayout;//Variaable qui contiendra le DrawerLayout
     MaterialDialog boiteDialogue;//boite de dialogue qui permet de choisir la ville


    private View navHeader;//variable qui contient le Nav_Header
    private TextView txtName, txtWebsite;//variable qui contiendrons le nom et l'email dans le nav_header
    private ImageView imgNavHeaderBg, imgProfile;//variable qui contiendront le photo de profil et de couverture dans le nav_header

    private MaterialViewPager mViewPager;
    private View headerLogo;
    private ImageView headerLogoContent;


    private SessionManager mSessionManager;//Variable qui contient la session
    private SQLiteHandler db;//Variable qui contient le base de donne
    public static String API_KEY;


    public static String NOM_VILLE;//variable qui va contenir la ville choisi pour la recherche du supermarché



    /**URL des photos de profils et photo de couverture**/
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = " ";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        //4 onglets
        final int tabCount = 5;

        //les vues définies dans @layout/header_logo
        headerLogo = findViewById(R.id.headerLogo);
        headerLogoContent = findViewById(R.id.headerLogoContent);

        //initialisation du du ViewPager

        mViewPager = findViewById(R.id.viewpager);


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 2:

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    case 4:

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;

            }



        });

        //CONFIGUARTION DE NOTRE ADAPTATUER DANS LE VIEW PAGER POUR AFFICHER CHAQUE FRAGMENT INDEPENDAMENT

        //remplir le ViewPager
        this.mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        //On retourne le fragment des prduit alimentaire
                        return Alimentaire_Fragment.newInstance();
                    case 1:
                        //On retourne le frgament des produit Maison et Decoration
                        return Maison_Decoration_Fragment.newInstance();
                    case 2:
                        //On retourne le frgaments des produits Mode et Beauté
                        return Mode_Beaute_Fragment.newInstance();
                    case 3:
                        //On retourne le frgaments des produits multimedia
                        return Multimedia_Fragment.newInstance();
                    case 4:
                        //On retourne le fragments des produits Sport et Loisir
                        return Sport_Loisir_Fragment.newInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {

                return tabCount;
            }

            //le titre à afficher pour chaque page
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Alimentaire";
                    case 1:
                        return "Maison & Decoration";
                    case 2:
                        return "Mode & Beauté";
                    case 3:
                        return "Multimedia";
                    case 4:
                        return "Sport & Loisir ";
                    default:
                        return null;
                }
            }
        });

        //permet au viewPager de garder 4 pages en mémoire (à ne pas utiliser sur plus de 4 pages !)

        this.mViewPager.getViewPager().setOffscreenPageLimit(tabCount);
        //relie les tabs au viewpager
        this.mViewPager.getPagerTitleStrip().setViewPager(this.mViewPager.getViewPager());
        //mViewPager.getPagerTitleStrip().setIndicatorColor(R.color.white);
        mViewPager.getPagerTitleStrip().setTextColorResource(R.color.white);
        mViewPager.getPagerTitleStrip().setIndicatorColorResource(R.color.white);



        this.mToolbar= findViewById(R.id.toolbar);//Initialisation du Toobar
        setSupportActionBar(mToolbar);//On attache le Toolbar dans l'activité


        mDrawerLayout= findViewById(R.id.drawer_layout);//Initialisation du DrawerLayout

        //Ajout du Bouton Humberguer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,mDrawerLayout,mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView= findViewById(R.id.nav_view);//Initalisation du Navigation Drawer


        //Ajout du Listener du Menu du NaviagtionView
        mNavigationView.setNavigationItemSelectedListener(this);


        //on selectionne aussi son l'item du menu principal par defaut

        mNavigationView.getMenu().getItem(0).setChecked(true);

        // Navigation view header
        navHeader = mNavigationView.getHeaderView(0);
        txtName =  navHeader.findViewById(R.id.name);
        txtWebsite =  navHeader.findViewById(R.id.website);
        imgNavHeaderBg =  navHeader.findViewById(R.id.img_header_bg);
        imgProfile = navHeader.findViewById(R.id.img_profile);


        loadNavHeader(); //fonction qui organise le Nav_header



        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext()); //Initialisation de la base de donne local

        // Verifiaction de la session
        mSessionManager = new SessionManager(getApplicationContext());

        if (!mSessionManager.isLoggedIn()) {
            logoutUser();
        }


        HashMap<String,String> infoUser=db.getUserDetails();
        txtName.setText(infoUser.get("name"));
        txtWebsite.setText(infoUser.get("email"));
        API_KEY=infoUser.get("api_key");
    }
    /**
     * Fonction qui permet la deconnexion de l'utilisateur ,il efface aussi ses donne dans base donné local
     * */
    private void logoutUser() {
        mSessionManager.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Evenement lorsqu'on appuiye sur le bouton de retour
     * ici on ferme le DrawerLayout s'il est ouvert
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Lorsqu'on selection un element (item) dans la Menu de Navigation

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // on prend l'id de l'item et on le met dans un vairiable
        int id = item.getItemId();

        //On fait un switch pour adapter a chaque item à une action
        switch (id){
            case R.id.nav_profil:
                //lorsque l'utilisateur appuye sur l'item du profil,on lance ProfilActivity
                Intent intent=new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_parametre:

               //lorsque l'utilisateur clique sur l'item des parametres ,on lance ParamtreActivity
                Intent intent1=new Intent(MainActivity.this,ParametresActivity.class);
               startActivity(intent1);
                break;
            case R.id.deconect:
                //Deconnexion de l'utilisateur
                Toast.makeText(this,"Deconnexion....",Toast.LENGTH_LONG).show();
                logoutUser();//appelle de la fonction deconexion;
                break;
            case R.id.menu_principal:

                //si l'utilisateur appuye sur l'item menu principal,on ferme le DrawerLayout
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                break;
        }

        //Lorsqu'on appuye sur un item on ferme aussi le Menu de Navigation avant d'aller dans sa page
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /***
     * Load navigation menu header information
     * like background image, profile image
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Nom Utilisateur");
        txtWebsite.setText("email@gmail.com");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)//application d'une image par defaut
                .apply(new RequestOptions().error(R.drawable.bg_gradient).placeholder(R.drawable.bg_gradient))
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .apply(RequestOptions.circleCropTransform())
                .apply(new RequestOptions().error(R.drawable.ic_user).placeholder(R.drawable.ic_user))
                .into(imgProfile);
    }

     //Creation du menu toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                 getMenuInflater().inflate(R.menu.menu_toolbar_activity_main,menu);
                 return  true;
    }

    //Evnement lors du clique d'un iteem du Menu toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ticket_item:

                boiteDialogue=new MaterialDialog.Builder(MainActivity.this)
                        .title("Quelle ville habitez-vous ? ")//Titre du boite de dialogue
                        .items(R.array.ville_maroc) //implementation du tableau des villes dans la listes
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                                /**
                                 * On prends le noms de la ville et on l'enregistre dans un clé
                                 *
                                 */
                                NOM_VILLE=text.toString();


                                return true;
                            }
                        })
                        .positiveText(R.string.valider)
                        .negativeText(R.string.Annuler)
                        .onPositive(new MaterialDialog.SingleButtonCallback() { //Quand on appuye sur le bouton "Valider"
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                /**
                                 * Rechercher tous les supermarché de la Ville en lançant l'activity Liste Supermarché
                                 */
                                Intent intent=new Intent(MainActivity.this,ListeSupermarche_Activity.class);
                                intent.putExtra("VILLE",NOM_VILLE);

                                startActivity(intent);


                            }
                        })
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }








}
