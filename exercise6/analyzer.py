from sqlalchemy import create_engine, func, Column, Integer, String, Date, Float, ForeignKey
from sqlalchemy.orm import sessionmaker, relationship, declarative_base
from sqlalchemy.schema import MetaData

# Replace these values with your actual database credentials
jdbc_user = 'vsisp69'
jdbc_pass = 'vAh5XRcX'
jdbc_host = 'vsisdb.informatik.uni-hamburg.de'
jdbc_port = 5432
jdbc_db = 'dis-2024'

# Database connection URL (without currentSchema)
db_url = f"postgresql://{jdbc_user}:{jdbc_pass}@{jdbc_host}:{jdbc_port}/{jdbc_db}"

# Set the default schema
schema_name = 'vsisp69'
metadata = MetaData(schema=schema_name)

# Create the SQLAlchemy engine
engine = create_engine(db_url, echo=True)  # Set echo=True for logging SQL statements

# Base class for declarative ORM models
Base = declarative_base(metadata=metadata)

# Define your SQLAlchemy models based on your database schema
class Country(Base):
    __tablename__ = 'Country'

    CountryID = Column(Integer, primary_key=True)
    Name = Column(String(255), nullable=False)

class Region(Base):
    __tablename__ = 'Region'

    RegionID = Column(Integer, primary_key=True)
    CountryID = Column(Integer, ForeignKey('Country.CountryID'), nullable=False)
    Name = Column(String(255), nullable=False)

    country = relationship("Country", back_populates="regions")

Country.regions = relationship("Region", order_by=Region.RegionID, back_populates="country")

class City(Base):
    __tablename__ = 'City'

    CityID = Column(Integer, primary_key=True)
    RegionID = Column(Integer, ForeignKey('Region.RegionID'), nullable=False)
    Name = Column(String(255), nullable=False)

    region = relationship("Region", back_populates="cities")

Region.cities = relationship("City", order_by=City.CityID, back_populates="region")

class Shop(Base):
    __tablename__ = 'Shop'

    ShopID = Column(Integer, primary_key=True)
    CityID = Column(Integer, ForeignKey('City.CityID'), nullable=False)
    Name = Column(String(255), nullable=False)

    city = relationship("City", back_populates="shops")

City.shops = relationship("Shop", order_by=Shop.ShopID, back_populates="city")

class ProductCategory(Base):
    __tablename__ = 'ProductCategory'

    ProductCategoryID = Column(Integer, primary_key=True)
    Name = Column(String(255), nullable=False)

class ProductFamily(Base):
    __tablename__ = 'ProductFamily'

    ProductFamilyID = Column(Integer, primary_key=True)
    ProductCategoryID = Column(Integer, ForeignKey('ProductCategory.ProductCategoryID'), nullable=False)
    Name = Column(String(255), nullable=False)

    product_category = relationship("ProductCategory", back_populates="product_families")

ProductCategory.product_families = relationship("ProductFamily", order_by=ProductFamily.ProductFamilyID, back_populates="product_category")

class ProductGroup(Base):
    __tablename__ = 'ProductGroup'

    ProductGroupID = Column(Integer, primary_key=True)
    ProductFamilyID = Column(Integer, ForeignKey('ProductFamily.ProductFamilyID'), nullable=False)
    Name = Column(String(255), nullable=False)

    product_family = relationship("ProductFamily", back_populates="product_groups")

ProductFamily.product_groups = relationship("ProductGroup", order_by=ProductGroup.ProductGroupID, back_populates="product_family")

class Article(Base):
    __tablename__ = 'Article'

    ArticleID = Column(Integer, primary_key=True)
    ProductGroupID = Column(Integer, ForeignKey('ProductGroup.ProductGroupID'), nullable=False)
    Name = Column(String(255), nullable=False)
    Price = Column(Float, nullable=False)

    product_group = relationship("ProductGroup", back_populates="articles")

ProductGroup.articles = relationship("Article", order_by=Article.ArticleID, back_populates="product_group")

class Sale(Base):
    __tablename__ = 'sales'

    SalesID = Column(Integer, primary_key=True)
    Date = Column(Date)
    ShopID = Column(Integer, ForeignKey('Shop.ShopID'))
    ArticleID = Column(Integer, ForeignKey('Article.ArticleID'))
    Sold = Column(Integer)
    Revenue = Column(Float)

    shop = relationship("Shop", back_populates="sales")
    article = relationship("Article", back_populates="sales")

Shop.sales = relationship("Sale", order_by=Sale.SalesID, back_populates="shop")
Article.sales = relationship("Sale", order_by=Sale.SalesID, back_populates="article")

class Purchase(Base):
    __tablename__ = 'purchase'

    PurchaseID = Column(Integer, primary_key=True)
    ShopID = Column(Integer, ForeignKey('Shop.ShopID'))
    ArticleID = Column(Integer, ForeignKey('Article.ArticleID'))
    Date = Column(Date)
    Amount = Column(Integer)
    Price = Column(Float)

    shop = relationship("Shop", back_populates="purchases")
    article = relationship("Article", back_populates="purchases")

Shop.purchases = relationship("Purchase", order_by=Purchase.PurchaseID, back_populates="shop")
Article.purchases = relationship("Purchase", order_by=Purchase.PurchaseID, back_populates="article")

# Create all tables in the database
Base.metadata.create_all(engine)

# Function to get a session
def get_session():
    Session = sessionmaker(bind=engine)
    return Session()

# Example function to generate cross table
def generate_cross_table(dimensions, measures):
    session = get_session()

    try:
        query = session.query()

        for dimension in dimensions:
            query = query.add_column(dimension)

        for measure in measures:
            query = query.add_column(func.sum(measure))

        query = query.group_by(*dimensions)

        result = query.all()

        # Print header
        header = "|"
        for dimension in dimensions:
            header += f" {dimension.key} |"
        for measure in measures:
            header += f" {measure.key} |"
        print(header)

        # Print rows
        for row in result:
            line = "|"
            for value in row:
                line += f" {value} |"
            print(line)

    except Exception as e:
        print(f"Error: {e}")
        session.rollback()
    finally:
        session.close()

# Example usage
if __name__ == "__main__":
    # Example of generating a cross table
    dimensions = [
        Country.Name,
        Region.Name,
        func.extract('quarter', Sale.Date).label('Quarter'),
        func.extract('year', Sale.Date).label('Year')
    ]
    measures = [Sale.Sold, Sale.Revenue]

    generate_cross_table(dimensions, measures)
