import mph

# Start a COMSOL client (this launches COMSOL in the background)
client = mph.start()

# Load your model file (replace with your actual .mph file path)
model = client.load('/home/penelope_quassolo/Downloads/DMRadio-CoreGUT-20260727T171715Z-1-001/DMRadio-CoreGUT/20260303_MagnetSim_18T_GUT.mph')

print("Model loaded successfully:", model.name())