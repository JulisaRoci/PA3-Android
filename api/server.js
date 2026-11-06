// API REST propia del Grupo 1 - PA3 Aplicaciones Moviles para Android
// Login con token JWT y CRUD de productos. Los datos se guardan en db.json.
const express = require("express");
const jwt = require("jsonwebtoken");
const fs = require("fs");
const path = require("path");
const os = require("os");

const PORT = process.env.PORT || 3000;
const SECRET = process.env.JWT_SECRET || "grupo1-pa3-secreto";
const DB_FILE = path.join(__dirname, "db.json");
const SEED_FILE = path.join(__dirname, "db.seed.json");

// ---------- Base de datos en archivo JSON ----------
function leerDb() {
  if (!fs.existsSync(DB_FILE)) fs.copyFileSync(SEED_FILE, DB_FILE);
  return JSON.parse(fs.readFileSync(DB_FILE, "utf8"));
}

function guardarDb(db) {
  fs.writeFileSync(DB_FILE, JSON.stringify(db, null, 2));
}

const app = express();
app.use(express.json());

// Muestra cada peticion en la consola
app.use((req, res, next) => {
  console.log(`${new Date().toLocaleTimeString()} ${req.method} ${req.url}`);
  next();
});

// ---------- Middleware: exige el token "Bearer" ----------
function verificarToken(req, res, next) {
  const header = req.headers.authorization || "";
  const token = header.startsWith("Bearer ") ? header.slice(7) : null;
  if (!token) return res.status(401).json({ mensaje: "Token requerido" });
  try {
    req.usuario = jwt.verify(token, SECRET);
    next();
  } catch (e) {
    return res.status(401).json({ mensaje: "Token invalido o expirado" });
  }
}

// ---------- Validacion de producto ----------
function validarProducto(body) {
  const { nombre, categoria, precio, stock } = body;
  if (!nombre || !String(nombre).trim()) return "El nombre es obligatorio";
  if (!categoria || !String(categoria).trim()) return "La categoria es obligatoria";
  if (precio === undefined || isNaN(Number(precio)) || Number(precio) < 0) return "El precio debe ser un numero mayor o igual a 0";
  if (stock === undefined || !Number.isInteger(Number(stock)) || Number(stock) < 0) return "El stock debe ser un entero mayor o igual a 0";
  return null;
}

function armarProducto(codigo, body) {
  return {
    codigo,
    nombre: String(body.nombre).trim(),
    categoria: String(body.categoria).trim(),
    precio: Number(body.precio),
    stock: Number(body.stock),
  };
}

// ---------- Rutas ----------
app.get("/", (req, res) => {
  res.json({ api: "Grupo 1 - PA3 Android", estado: "activa" });
});

// Login: valida usuario y clave, y devuelve un token JWT
app.post("/api/auth/login", (req, res) => {
  const { usuario, clave } = req.body || {};
  if (!usuario || !clave) return res.status(400).json({ mensaje: "Usuario y clave son obligatorios" });

  const db = leerDb();
  const encontrado = db.usuarios.find(
    (u) => u.usuario.toLowerCase() === String(usuario).toLowerCase() && u.clave === clave
  );
  if (!encontrado) return res.status(401).json({ mensaje: "Usuario o contraseña incorrectos" });
  if (!encontrado.estado) return res.status(403).json({ mensaje: "El usuario se encuentra inactivo" });

  const token = jwt.sign(
    { sub: encontrado.usuario, codigo: encontrado.codigo, rol: encontrado.rol },
    SECRET,
    { expiresIn: "8h" }
  );
  const { clave: _omitida, ...datos } = encontrado;
  res.json({ token, usuario: datos });
});

// CRUD de productos (todas las rutas necesitan token)
app.get("/api/productos", verificarToken, (req, res) => {
  res.json(leerDb().productos);
});

app.get("/api/productos/:codigo", verificarToken, (req, res) => {
  const producto = leerDb().productos.find((p) => p.codigo === Number(req.params.codigo));
  if (!producto) return res.status(404).json({ mensaje: "Producto no encontrado" });
  res.json(producto);
});

app.post("/api/productos", verificarToken, (req, res) => {
  const error = validarProducto(req.body || {});
  if (error) return res.status(400).json({ mensaje: error });

  const db = leerDb();
  const codigo = db.productos.reduce((max, p) => Math.max(max, p.codigo), 0) + 1;
  const nuevo = armarProducto(codigo, req.body);
  db.productos.push(nuevo);
  guardarDb(db);
  res.status(201).json(nuevo);
});

app.put("/api/productos/:codigo", verificarToken, (req, res) => {
  const error = validarProducto(req.body || {});
  if (error) return res.status(400).json({ mensaje: error });

  const db = leerDb();
  const codigo = Number(req.params.codigo);
  const indice = db.productos.findIndex((p) => p.codigo === codigo);
  if (indice === -1) return res.status(404).json({ mensaje: "Producto no encontrado" });

  db.productos[indice] = armarProducto(codigo, req.body);
  guardarDb(db);
  res.json(db.productos[indice]);
});

app.delete("/api/productos/:codigo", verificarToken, (req, res) => {
  const db = leerDb();
  const codigo = Number(req.params.codigo);
  const indice = db.productos.findIndex((p) => p.codigo === codigo);
  if (indice === -1) return res.status(404).json({ mensaje: "Producto no encontrado" });

  db.productos.splice(indice, 1);
  guardarDb(db);
  res.status(204).send();
});

// ---------- Inicio del servidor ----------
app.listen(PORT, "0.0.0.0", () => {
  const ips = Object.values(os.networkInterfaces())
    .flat()
    .filter((i) => i && i.family === "IPv4" && !i.internal)
    .map((i) => i.address);
  console.log(`API Grupo 1 escuchando en el puerto ${PORT}`);
  ips.forEach((ip) => console.log(`  Desde el celular (misma Wi-Fi): http://${ip}:${PORT}/`));
  console.log(`  Desde el emulador: http://10.0.2.2:${PORT}/`);
});
