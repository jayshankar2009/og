package com.synergy.keimed_ordergenie.app;

public class AppConfig {

    public static final String IMAGE_DIRECTORY_NAME = "Ordergenie";
    //public static String URL_IPADDRESS = "http://198.50.198.184/medikartapi" ;
    //"http://www.ordergenie.co.in/api/UserMasters/2/password
    /*---------------apurva-----------------*/
    public static  final int AppCode =0; /// 0 for OG and Keimed
                                         //  1 for Unnati

 public static String URL_IPADDRESS = "http://orderconnect.ordergenie.co.in:8080";
  //  public static String URL_IPADDRESS = "http://13.126.247.150:8081";// 17 sept 2018 NEW UAT SERVER // Anjani
     //public static String URL_IPADDRESS = "http://13.232.102.14:8080";//UAT SERVER
  //public static String URL_IPADDRESS = "http://13.232.215.9:8080";// new UAT SERVER

    public static String getOrderReportSalesmanWithDate = URL_IPADDRESS+"/api/AppApis/getOrderReportSalesmanWithDate/";
   // public static String URL_IPADDRESS = "http://192.168.31.151:9000";// new LOcal SERVER
  //  public static String URL_IPADDRESS = "http://www.ordergenie.co.in";
    public static String checklogin = URL_IPADDRESS + "/auth/local";
    public static String checkpasswordchange = URL_IPADDRESS + "/api/UserMasters/2/password/";
    public static String GET_ME = URL_IPADDRESS + "/api/usermasters/userinfo";
    public static String FORGOT_PASSWORD = URL_IPADDRESS + "/api/UserMasters/forgotPassword/";
    public static String GET_UNMAPPED_CHEMIST = URL_IPADDRESS + "/api/stockistchemisth mappings/getUnMappedChemist/";
    public static String GET_PINCODE_SEARCH = URL_IPADDRESS + "/api/PincodeMasters/getPinCodeList/";
    public static String GET_CITY_STATE_ON_PINCODE = URL_IPADDRESS + "/api/PincodeMasters/getCityStateDetails/";
    public static String POST_SIGN_UP = URL_IPADDRESS + "/api/clientmasters/signup";
    public static String GET_STOCKIST_ORDER_LIST = URL_IPADDRESS + "/api/orders/getOrdersDist/";
    public static String GET_CHEMIST_STOCKIST = URL_IPADDRESS + "/api/clientmasters/APP_GetChemistStockistList/";
    public static String GET_CHEMIST_ORDERLIST = URL_IPADDRESS + "/api/orders/APP_GetChemistOrders/";


    public static String GET_SALES_SUMMARY_LIST =URL_IPADDRESS + "/api/UserMasters/APP_getSalesData/";
    public static String GET_CHEMIST_ORDERL_DETAILS = URL_IPADDRESS + "/api/orderdetails/APP_OrderDetails/";

    public static String GET_CHEMIST_PEDNING_BILLS = URL_IPADDRESS + "/api/CustomerBills/APP_GetChemistPendingBills/";

    public static String POST_CHEMIST_TO_STOCKIST_INVENTORY_ACESS = URL_IPADDRESS + "/api/notifications";
 //   public static String GET_CHEMIST_PRODUCT_DATA = URL_IPADDRESS + "/api/products/APP_GetFullProductSearch_V2/";
    public static String GET_CHEMIST_PRODUCT_DATA = URL_IPADDRESS + "/api/products/APP_GetFullProductSearch/";
  //  public static String GET_CHEMIST_PRODUCT_SEARCH = URL_IPADDRESS + "/api/products/APP_GetFullProductSearch/";

    public static String GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DATA = URL_IPADDRESS + "/api/products/getSearchedProdsAndMolecules/";

    public static String GET_FULL_UNMAPPED_CHEMIST_PRODUCT_DETAIL = URL_IPADDRESS + "/api/products/App_ProductDetails/";

    public static String POST_CHEMIST_CONFIRM_ORDER = URL_IPADDRESS + "/api/orders";
    public static String POST_CHEMIST_CONFIRM_ORDER_APPWEB = URL_IPADDRESS + "/api/orders/APP_WEB_CreateOrder";

    public static String GET_DISTRIBUTOR_PAYMENTS = URL_IPADDRESS + "/api/AppApis/getpaymentdetails/";

    public static String GET_DISTRIBUTOR_SALESRETURNS = URL_IPADDRESS + "/api/AppApis/getproductreturns/";

    public static String GET_STOCKIST_INVENTORY = URL_IPADDRESS + "/api/productmappings/app_GetProducts/";      //----------
    public static String GET_SALESMAN_INVENTORY = URL_IPADDRESS + "/api/products/APP_GetFullProductSearch/";      //----------
    //public static String GET_SALESMAN_INVENTORY = URL_IPADDRESS + "/api/products/APP_GetFullProductSearch_V2/";

   // http://orderconnect.ordergenie.co.in:8080/api/products/APP_GetFullProductSearch/[1,2,"",-99,"M"]

    //public static String GET_STOCKIST_INVENTORY = "http://192.168.10.119:9000" + "/api/productmappings/app_GetProducts/";
    public static String GET_DISTRIBUTOR_PRODUCTS = URL_IPADDRESS + "/api/AppApis/getStockistProductList/";

    public static String GET_STOCKIST_CUSTOMERLIST = URL_IPADDRESS + "/api/clientmasters/GetStockistCustomerList/";

    public static String GET_STOCKIST_CUSTOMERLIST_STOCKISTLOGIN = URL_IPADDRESS + "/api/stockistchemistmappings/getChemists/";

    public static String GET_CUSTOMERLIST_DISTRIBUTORLOGIN = URL_IPADDRESS + "/api/AppApis/customerlistwithpendingbills/";

    public static String GET_STOCKIST_INDIVIDUAL_PENDINGLIST = URL_IPADDRESS + "/api/CustomerBills/GetIndivisualPendingBills/";
 // public static String GET_STOCKIST_PENDING_BILLS ="http://192.168.0.117:9000/api/CustomerBills/APP_GetStockistPendingBills/";
    public static String GET_STOCKIST_PENDING_BILLS = URL_IPADDRESS + "/api/CustomerBills/APP_GetStockistPendingBills/";

    public static String GET_STOCKIST_STASTICS = URL_IPADDRESS + "/api/usermasters/APP_StockistUserStats/";
    public static String GET_DASHBOARD_STASTICS_SALESMAN = URL_IPADDRESS + "/api/appapis/SalesmanStats/";
    public static String GET_STOCKIST_INDIVIDUAL_ORDER_SUMMARY = URL_IPADDRESS + "/api/orders/APP_GetStockistChemistOrderSummary/";

    // public static String GET_DISTRIBUTOR_PENDING_BILL_LIST = URL_IPADDRESS + "/api/CustomerBills/getStockistBills/";
    public static String GET_DISTRIBUTOR_PENDING_BILL_LIST = URL_IPADDRESS + "/api/AppApis/getStockistBills/";

    public static String GET_STOCKIST_INDIVIDUAL_ORDER_HISTORY = URL_IPADDRESS + "/api/orders/APP_GetStockistOrderHistory/";
    public static String newcheckpasswordchange = URL_IPADDRESS + "/api/UserMasters/changePassword_App/2/password/";
    public static String GET_STOCKIST_CALL_PLAN = URL_IPADDRESS + "/api/stockistcallplandetails/getUserCallPlanDetails/";

    public static String GET_PRODUCT_BY_MOLECULE = URL_IPADDRESS + "/api/Molecules/getProductByMolecule/";

    public static String GET_DRUG_DRUG_INTRACTION_BY_MOLECULE = URL_IPADDRESS + "/api/Molecules/getDrugDrugInteractionDataByMolecule/";

    public static String GET_DRUG_MOLECULE_INTRACTION_BY_MOLECULE = URL_IPADDRESS + "/api/Molecules/getDrugMoleculeInteractionDatabyMolecule/";

    public static String GET_MOLECULE_GENERAL_INFO_BY_MOLECULE = URL_IPADDRESS + "/api/Molecules/getMoleculeGeneralInfo/";

    public static String GET_PRODUCT_DETAIL = URL_IPADDRESS + "/api/Molecules/APP_GetDrugDetails/";

    public static String EDIT_PROFILE = URL_IPADDRESS + "/api/UserMasters/";

    public static String GET_TASKS = URL_IPADDRESS + "/api/taskmasters";
    public static String GET_DISTRIBUTOR_ORDER_DETAILS = URL_IPADDRESS + "/api/orderdetails/getOrdersDetails/";
    public static String GET_MOLECULE_DATA = URL_IPADDRESS + "/api/Molecules/APP_GetMoleculeDetails/";

    public static String POST_STOCKIST_LEGENDS = URL_IPADDRESS + "/api/productstocklegnedmaster/getStockistLegendByStockistID";

    // public static String POST_UPDATE_USER_LOCATION = URL_IPADDRESS +"sai";
    public static String POST_UPDATE_USER_LOCATION = URL_IPADDRESS + "/api/usermasters/UpdateUserLocation";
    public static String POST_GET_STOCKIST_LEGENDS = URL_IPADDRESS + "/api/productstocklegnedmaster/getStockistLegendByStockistID"; //-----

    public static String POST_SAVE_CALL_PLAN = URL_IPADDRESS + "/api/StockistUserCallPlanStatus";

    public static String GET_USER_OFFERS = URL_IPADDRESS + "/api/offers/APP_getUserOffers/";

    public static String GET_USER_NOTIFICATIONS = URL_IPADDRESS + "/api/applicationnotifications/getApplicationNoticiation/";

    public static String GET_DISTRIBUTOR_NOTIFICATIONS = URL_IPADDRESS + "/api/AppApis/GetNotificationStokiest/";
    public static String POST_UPDATE_CHEMIST_LOCATION = URL_IPADDRESS + "/api/clientmasters/UpdateChemistLocation";
    public static String GET_ALL_LEGEND_DATA = URL_IPADDRESS + "/api/clientmasters/APP_getStockistsListWithLegendsList";

    public static String GET_SALES_RETURNS = URL_IPADDRESS + "/api/orders/getsalesreturnreport";

    public static String SAVE_IMAGE_PROFILE = URL_IPADDRESS + "/api/usermasters/uploadAvatar";

    public static String GET_PARTIAL_CHEMIST_DATA = URL_IPADDRESS + "/api/products/APP_GetPartialProductSearch/";

    public static String SAVE_PAYMENT = URL_IPADDRESS + "/api/StockistPayments";
    public static String CHEMIST_PRODUCT_SEARCH = URL_IPADDRESS+"/api/products/APP_GetFullProductSearch/";

    public static String SAVE_ORDER_DELIVERY = URL_IPADDRESS + "/api/orders/APP_updateOrderStatus";

    public static String SAVE_ORDER_DELIVERY_SALESMAN = URL_IPADDRESS + "/api/DeliverySchedule/UpdateDelivery_status";

    public static String SAVE_ORDER_INVOICE = URL_IPADDRESS + "/api/InvoiceDetails/APP_updateInvoiceStatus";

    public static String STOCKIST_INVOICE_ITEM_BYINOICEID = URL_IPADDRESS + "/api/InvoiceItemDetails/getInvoiceDetailsByInvoiceID/";

    public static String UPDATE_USER_PROFILE = URL_IPADDRESS + "api/UserMasters/UpdateUserInfo";

    public static String DISTRIBUTOR_ORDERS = URL_IPADDRESS + "/api/orders/getOrdersDist/";

    public static String DISTRIBUTOR_DASHBOARD_DAYWISE_DATA = URL_IPADDRESS + "/api/AppApis/completedaywisedata/";
    public static String DISTRIBUTOR_DASHBOARD_WEEKWISE_DATA=URL_IPADDRESS+"/api/AppApis/completeweekwisedata/";
    public static String DISTRIBUTOR_DASHBOARD_MONTHWISE_DATA=URL_IPADDRESS+"/api/AppApis/completemonthwisedata/";
    // http://www.ordergenie.co.in/api/UserMasters/UpdateUserInfo[UserID,ClientID,UserName]
    public static String GET_DISTRIBUTOR_USERS_LIST = URL_IPADDRESS + "/api/AppApis/userlistNcallplan/";
    public static String GET_DISTRIBUTOR_CUSTOMER_SALECHART = URL_IPADDRESS + "/api/AppApis/customerSalesChartYearly/";
    public static String GET_DISTRIBUTOR_MONTH_GRAPH = URL_IPADDRESS +"/api/AppApis/dashboardchartmonthwise/";
    public  static  String SALES_RETURN_PRODUCT_LIST="http://192.168.0.120:9000/api/orderdetails/getOrderProdOfDist/";
    public static String GetUpdatedProductList = URL_IPADDRESS + "/api/products/APP_GetPartialProductSearch/";
    public static String SALES_RETURNS_REQUEST = "http://192.168.0.120:9000/api/ProductReturns";

    public static String GetPendingBillBySalesman = URL_IPADDRESS + "/api/appapis/PendingBillbySalsman/";
    public static String GetUpdatedPendingBillBySalesman = URL_IPADDRESS + "/api/appapis/GetPartialPayment/";
    public static String CollectPayment = URL_IPADDRESS + "/api/payments";
   // public static String DeliveryScheduleSalesman = URL_IPADDRESS + "/api/DeliverySchedule/Get_DeliverySchedule/";
    public static String DeliveryScheduleSalesman = URL_IPADDRESS + "/api/DeliverySchedule/Get_DeliverySchedule/";
    public static String DailyCollections = URL_IPADDRESS + "/api/payments/DailyCollections/";
    public static String POST_UPDATE_CURRENT_LOCATION = URL_IPADDRESS + "/api/usermasters/UpdateUserLocation_V1";
    //http://orderconnect.ordergenie.co.in:8080/api/appapis/GetPartialPayment/[36596,"2018-01-10T11:17:15"]
    //http://orderconnect.ordergenie.co.in:8080/api/DeliverySchedule/Get_DeliverySchedule/[39471,"2018-05-31"]
    public static String GET_CURRENT_LOCATION = URL_IPADDRESS + "/api/stockistcallplandetails/insertGetSalesmanLocation/";
    public static String GET_UPDATED_VERSION = URL_IPADDRESS + "/api/appversions/";
    public static String DOWNLOAD_APK = URL_IPADDRESS + "/apk/app-debug.apk";
    public static String GET_CHEMIST_TO_STOCKIST_INVENTORY_ACESS = URL_IPADDRESS + "/api/notifications/StocistSendRequest/";
    public static String GET_SUMMARY_LIST =URL_IPADDRESS + "/api/AppApis/getOrderReportSalesman/";
}

