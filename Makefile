SYSJ=a.sysj
CONF=a.xml

CV_DIR=src/conveyor
F_DIR=src/filler
CP_DIR=src/capper
R_DIR=src/rotary
B_DIR=src/baxter_left

CV=root@192.168.1.101
F=root@192.168.1.102
R=root@192.168.1.103
CP=root@192.168.1.104

BASE=bs
REMOTE=true
SYSJ_DIR=systemj
BIN=$(SYSJ_DIR)/sjdk/bin
LIB=$(SYSJ_DIR)/sjdk/lib
TOOLS=$(BIN) $(LIB)
SJDK_RELEASE=https://github.com/hjparker/systemj-release/releases/download/2.1-113/sjdk-v2.1-113-ga85cf96.zip

all: systemj/sjdk init build_all run

systemj/sjdk:
	-mkdir $(SYSJ_DIR)
	cd $(SYSJ_DIR); wget $(SJDK_RELEASE); unzip sjdk*.zip; mv sjdk*/ sjdk
	chmod 777 $(BIN)/* $(LIB)/*
	javac -cp $(LIB)/\* gpio/com/systemj/ipc/*.java
	cd gpio; jar uvf ../$(LIB)/sjrt*-desktop.jar com/systemj/ipc/*.class

clean:
ifeq ($(REMOTE),true)
	ssh $(CV) "rm -rf $(BASE)"
	ssh $(F)  "rm -rf $(BASE)"
	ssh $(R)  "rm -rf $(BASE)"
	ssh $(CP) "rm -rf $(BASE)"
endif
	rm -rf bin
	find src -type f \( -iname '*.java' -o -iname '*.class' \) -exec rm -rf {} \;
	rm -rf gpio/com/systemj/ipc/*.class
	rm -rf GUI/*.class

init:
ifeq ($(REMOTE),true)
	-ssh $(CV) "mkdir $(BASE)"
	-ssh $(F)  "mkdir $(BASE)"
	-ssh $(R)  "mkdir $(BASE)"
	-ssh $(CP) "mkdir $(BASE)"
	-scp -r $(TOOLS) $(CV):~/$(BASE)
	-scp -r $(TOOLS) $(F):~/$(BASE)
	-scp -r $(TOOLS) $(R):~/$(BASE)
	-scp -r $(TOOLS) $(CP):~/$(BASE)
endif

run:
ifeq ($(REMOTE),true)
ifeq ($(OS),Windows_NT)
	mintty --size=40,10 -p 0,0 -T conveyor -e ssh $(CV) -t "bash -l -c \"./start_fpga.sh; cd bs; killall java; bin/sysjr a.xml; read -p 'done'\"" & disown
	mintty --size=40,10 -p 400,0 -T filler -e ssh $(F) -t "bash -l -c \"./start_fpga.sh; cd bs;  killall java; bin/sysjr a.xml; read -p 'done'\""  & disown
	mintty --size=40,10 -p 800,0 -T rotary -e ssh $(R) -t "bash -l -c \"./start_fpga.sh; cd bs;  killall java; bin/sysjr a.xml; read -p 'done'\""  & disown
	mintty --size=40,10 -p 0,500 -T capper -e ssh $(CP) -t "bash -l -c \"./start_fpga.sh; cd bs; killall java; bin/sysjr a.xml; read -p 'done'\"" & disown
	mintty --size=40,10 -p 400,500 -T baxter -e bash -c "cd $(B_DIR); sysjr a.xml; read -p 'done'"  & disown
	cd webserver ; make
else
	$(error Need to implement deployment method for $(shell uname -s))
endif
endif

reset:
ifeq ($(OS),Windows_NT)
	mintty --size=20,10 -e ssh $(CV) -t "bash -l -c \"./start_fpga.sh\"" & disown
	mintty --size=20,10 -e ssh $(F) -t "bash -l -c \"./start_fpga.sh\""  & disown
	mintty --size=20,10 -e ssh $(R) -t "bash -l -c \"./start_fpga.sh\""  & disown
	mintty --size=20,10 -e ssh $(CP) -t "bash -l -c \"./start_fpga.sh\"" & disown
else
	ssh $(CV) -t "bash -l -c \"./start_fpga.sh\"" & disown
	ssh $(F) -t "bash -l -c \"./start_fpga.sh\""  & disown
	ssh $(R) -t "bash -l -c \"./start_fpga.sh\""  & disown
	ssh $(CP) -t "bash -l -c \"./start_fpga.sh\"" & disown
endif


build_all: $(CV_DIR)/Conveyor.class $(F_DIR)/filler.class $(R_DIR)/rotary.class $(CP_DIR)/Capper.class \
					 $(B_DIR)/Baxter.class

$(CV_DIR)/Conveyor.class: $(CV_DIR)/$(SYSJ)
	$(BIN)/sysjc -d $(CV_DIR) --silence $(CV_DIR)/$(SYSJ)
ifeq ($(REMOTE),true)
	scp -r $(CV_DIR)/* $(CV):~/$(BASE)
endif

$(F_DIR)/filler.class: $(F_DIR)/$(SYSJ)
	$(BIN)/sysjc -d $(F_DIR) --silence $(F_DIR)/$(SYSJ)
ifeq ($(REMOTE),true)
	scp -r $(F_DIR)/* $(F):~/$(BASE)
endif

$(R_DIR)/rotary.class: $(R_DIR)/$(SYSJ)
	$(BIN)/sysjc -d $(R_DIR) --silence $(R_DIR)/$(SYSJ)
ifeq ($(REMOTE),true)
	scp -r $(R_DIR)/* $(R):~/$(BASE)
endif

$(CP_DIR)/Capper.class: $(CP_DIR)/$(SYSJ)
	$(BIN)/sysjc -d $(CP_DIR) --silence $(CP_DIR)/$(SYSJ)
ifeq ($(REMOTE),true)
	scp -r $(CP_DIR)/* $(CP):~/$(BASE)
endif

$(B_DIR)/Baxter.class: $(B_DIR)/$(SYSJ)
	$(BIN)/sysjc --silence -d $(B_DIR) $(B_DIR)/a.sysj




