package com.synergy.keimed_ordergenie.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "STOCKIST_PRODUCTS".
 */
public class StockistProductsDao extends AbstractDao<StockistProducts, Long> {

    public static final String TABLENAME = "STOCKIST_PRODUCTS";

    /**
     * Properties of entity StockistProducts.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Itemcode = new Property(1, String.class, "Itemcode", false, "ITEMCODE");
        public final static Property Stockist_id = new Property(2, String.class, "Stockist_id", false, "STOCKIST_ID");
        public final static Property Product_ID = new Property(3, String.class, "Product_ID", false, "PRODUCT__ID");
        public final static Property Itemname = new Property(4, String.class, "Itemname", false, "ITEMNAME");
        public final static Property Packsize = new Property(5, String.class, "Packsize", false, "PACKSIZE");
        public final static Property MRP = new Property(6, String.class, "MRP", false, "MRP");
        public final static Property Rate = new Property(7, String.class, "Rate", false, "RATE");
        public final static Property Stock = new Property(8, String.class, "Stock", false, "STOCK");
        public final static Property Type = new Property(9, String.class, "type", false, "TYPE");
        public final static Property MfgCode = new Property(10, String.class, "MfgCode", false, "MFG_CODE");
        public final static Property MfgName = new Property(11, String.class, "MfgName", false, "MFG_NAME");
        public final static Property DoseForm = new Property(12, String.class, "DoseForm", false, "DOSE_FORM");
        public final static Property Scheme = new Property(13, String.class, "Scheme", false, "SCHEME");
    }


    public StockistProductsDao(DaoConfig config) {
        super(config);
    }

    public StockistProductsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"STOCKIST_PRODUCTS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ITEMCODE\" TEXT," + // 1: Itemcode
                "\"STOCKIST_ID\" TEXT," + // 2: Stockist_id
                "\"PRODUCT__ID\" TEXT," + // 3: Product_ID
                "\"ITEMNAME\" TEXT," + // 4: Itemname
                "\"PACKSIZE\" TEXT," + // 5: Packsize
                "\"MRP\" TEXT," + // 6: MRP
                "\"RATE\" TEXT," + // 7: Rate
                "\"STOCK\" TEXT," + // 8: Stock
                "\"TYPE\" TEXT," + // 9: type
                "\"MFG_CODE\" TEXT," + // 10: MfgCode
                "\"MFG_NAME\" TEXT," + // 11: MfgName
                "\"DOSE_FORM\" TEXT," + // 12: DoseFormz
                "\"SCHEME\" TEXT);"); // 13: Scheme
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STOCKIST_PRODUCTS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StockistProducts entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String Itemcode = entity.getItemcode();
        if (Itemcode != null) {
            stmt.bindString(2, Itemcode);
        }

        String Stockist_id = entity.getStockist_id();
        if (Stockist_id != null) {
            stmt.bindString(3, Stockist_id);
        }

        String Product_ID = entity.getProduct_ID();
        if (Product_ID != null) {
            stmt.bindString(4, Product_ID);
        }

        String Itemname = entity.getItemname();
        if (Itemname != null) {
            stmt.bindString(5, Itemname);
        }

        String Packsize = entity.getPacksize();
        if (Packsize != null) {
            stmt.bindString(6, Packsize);
        }

        String MRP = entity.getMRP();
        if (MRP != null) {
            stmt.bindString(7, MRP);
        }

        String Rate = entity.getRate();
        if (Rate != null) {
            stmt.bindString(8, Rate);
        }

        String Stock = entity.getStock();
        if (Stock != null) {
            stmt.bindString(9, Stock);
        }

        String type = entity.getType();
        if (type != null) {
            stmt.bindString(10, type);
        }

        String MfgCode = entity.getMfgCode();
        if (MfgCode != null) {
            stmt.bindString(11, MfgCode);
        }

        String MfgName = entity.getMfgName();
        if (MfgName != null) {
            stmt.bindString(12, MfgName);
        }

        String DoseForm = entity.getDoseForm();
        if (DoseForm != null) {
            stmt.bindString(13, DoseForm);
        }

        String Scheme = entity.getScheme();
        if (Scheme != null) {
            stmt.bindString(14, Scheme);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StockistProducts entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String Itemcode = entity.getItemcode();
        if (Itemcode != null) {
            stmt.bindString(2, Itemcode);
        }

        String Stockist_id = entity.getStockist_id();
        if (Stockist_id != null) {
            stmt.bindString(3, Stockist_id);
        }

        String Product_ID = entity.getProduct_ID();
        if (Product_ID != null) {
            stmt.bindString(4, Product_ID);
        }

        String Itemname = entity.getItemname();
        if (Itemname != null) {
            stmt.bindString(5, Itemname);
        }

        String Packsize = entity.getPacksize();
        if (Packsize != null) {
            stmt.bindString(6, Packsize);
        }

        String MRP = entity.getMRP();
        if (MRP != null) {
            stmt.bindString(7, MRP);
        }

        String Rate = entity.getRate();
        if (Rate != null) {
            stmt.bindString(8, Rate);
        }

        String Stock = entity.getStock();
        if (Stock != null) {
            stmt.bindString(9, Stock);
        }

        String type = entity.getType();
        if (type != null) {
            stmt.bindString(10, type);
        }

        String MfgCode = entity.getMfgCode();
        if (MfgCode != null) {
            stmt.bindString(11, MfgCode);
        }

        String MfgName = entity.getMfgName();
        if (MfgName != null) {
            stmt.bindString(12, MfgName);
        }

        String DoseForm = entity.getDoseForm();
        if (DoseForm != null) {
            stmt.bindString(13, DoseForm);
        }

        String Scheme = entity.getScheme();
        if (Scheme != null) {
            stmt.bindString(14, Scheme);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public StockistProducts readEntity(Cursor cursor, int offset) {
        StockistProducts entity = new StockistProducts( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Itemcode
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Stockist_id
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Product_ID
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Itemname
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Packsize
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // MRP
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Rate
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Stock
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // type
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // MfgCode
                cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // MfgName
                cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // DoseForm
                cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13),// Scheme
                cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // HalfScheme
                cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // PercentScheme
                 cursor.isNull(offset + 14) ? null : cursor.getString(offset + 16), // LegendName
                cursor.isNull(offset + 15) ? null : cursor.getString(offset + 17), // LegendColor
                cursor.isNull(offset + 16) ? null : cursor.getString(offset + 18),
                cursor.isNull(offset + 17) ? null : cursor.getString(offset + 19),//min
                cursor.isNull(offset + 18) ? null : cursor.getString(offset + 20)//max
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, StockistProducts entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setItemcode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStockist_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProduct_ID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setItemname(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPacksize(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setMRP(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStock(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setType(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setMfgCode(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setMfgName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setDoseForm(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setScheme(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setHalfScheme(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPercentScheme(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));

    }

    @Override
    protected final Long updateKeyAfterInsert(StockistProducts entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(StockistProducts entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(StockistProducts entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }


    public List getStockistProducts(String stockistId) {
        QueryBuilder<StockistProducts> qb = this.queryBuilder();
        qb.where(Properties.Stockist_id.eq(stockistId));
        return qb.list();

    }

    public void deleteStockistProducts(String stockistId) {
        QueryBuilder<StockistProducts> qb = this.queryBuilder();
        qb.where(Properties.Stockist_id.eq(stockistId));
        deleteInTx(qb.list());

    }


    public void updateProducts(String productID) {
        QueryBuilder<StockistProducts> qb = this.queryBuilder();
        qb.where(Properties.Stockist_id.eq(productID));
       // insertOrReplace();

    }

}